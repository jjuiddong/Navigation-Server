
#include "stdafx.h"
#include "path.h"

#pragma comment( lib, "libmysql.lib" )

struct sPathFile 
{
	uint64 timeId;
	string fileName;
};

int InsertPathData();
int InsertJourneyData();
int InsertLandMark();
bool QueryInsertPathData(MySQLConnection &sqlCon, cPath::sRow &row
	, const unsigned __int64 journeyTimeId);
double WGS84Distance(const Vector2d &lonLat0, const Vector2d &lonLat1);
const int userId = 1;


int main()
{
    std::cout << "navi DB Upload!\n"; 
	//InsertPathData();
	//InsertJourneyData();
	InsertLandMark();
	return 0;
}


int InsertLandMark()
{
	MySQLConnection sqlCon;
	if (!sqlCon.Connect("localhost", 3306, "navi", "1111", "navi"))
	{
		std::cout << "DB Connection Error\n";
		return 0;
	}

	cSimpleData file("../landmark.txt");
	if (!file.IsLoad())
	{
		std::cout << "not found landmark.txt file\n";
		return 0;
	}

	std::cout << "Start Upload LandMark\n";

	cDateTime2 date;
	date.UpdateCurrentTime();
	const string dateStr = cDateTime2::GetTimeStr3(date.GetTimeInt64()).c_str();

	for (auto &row : file.m_table)
	{
		if (row.size() < 3)
			continue; // error

		Vector2d lonLat;
		lonLat.x = atof(row[1].c_str());
		lonLat.y = atof(row[2].c_str());

		const string sql =
			common::format("INSERT INTO landmark (user_id, date_time, lon, lat)"
				" VALUES ('%d', '%s', '%f', '%f');"
				, userId, dateStr.c_str(), lonLat.x, lonLat.y);

		MySQLQuery query(&sqlCon, sql);
		query.ExecuteInsert();
	}

	std::cout << "Finish Upload LandMark\n";

	return 0;
}


int InsertPathData()
{
	list<string> exts;
	exts.push_back(".txt");
	list<string> files;
	common::CollectFiles(exts, "..\\path", files);

	MySQLConnection sqlCon;
	if (!sqlCon.Connect("localhost", 3306, "navi", "1111", "navi"))
	{
		std::cout << "DB Connection Error\n";
		return 0;
	}

	set<string> chkInsertData;

	for (auto &fileName : files)
	{
		if (chkInsertData.end() != chkInsertData.find(fileName))
			continue; // already insert

		cPath file0;
		const uint64 dateTime = file0.GetTimeId(fileName);
		if (dateTime == 0)
			continue; // error occurred!

		// format : yyyy-mm-dd
		const string dateStr1 = cDateTime2::GetTimeStr4(dateTime).c_str();
		// format : yyyymmdd
		const string dateStr2 = cDateTime2::GetTimeStr5(dateTime).c_str();

		// find all same date data (by time_id)
		vector<sPathFile> pathFiles;
		for (auto &fn : files)
		{
			cPath file;
			const uint64 timeId = file.GetTimeId(fn);
			if (dateStr1 == cDateTime2::GetTimeStr4(timeId).c_str())
			{
				cPath file;
				const uint64 timeId = file.GetTimeId(fn);
				if (timeId == 0)
					continue; // error occurred!
				pathFiles.push_back({ timeId, fn });
			}
		}
		if (pathFiles.empty())
			continue; // error occurred!

		// sorting by timeId
		std::sort(pathFiles.begin(), pathFiles.end(), 
			[](auto &a1, auto &a2) {return a1.timeId < a2.timeId;});

		const uint64 journeyTimeId = pathFiles[0].timeId;

		// calc all same date data
		for (auto &pathFile : pathFiles)
		{
			chkInsertData.insert(pathFile.fileName);

			cPath path;
			if (!path.Read(pathFile.fileName))
				continue;

			std::cout << "upload " << pathFile.fileName << "\n";

			for (uint i=1; i < path.m_table.size(); ++i)
			{
				{
					cPath::sRow &r0 = path.m_table[i - 1];
					cPath::sRow &r1 = path.m_table[i];
					const double d = WGS84Distance(r0.lonLat, r1.lonLat);
					if (d > 500)
						continue; // maybe lon/lat data crack
				}

				if (i==1)
					QueryInsertPathData(sqlCon, path.m_table[0], journeyTimeId);
				QueryInsertPathData(sqlCon, path.m_table[i], journeyTimeId);
			}
		}
	}

	std::cout << "finish~\n";
	return 1;
}


// upload journey_date table data
int InsertJourneyData()
{
	list<string> exts;
	exts.push_back(".txt");
	list<string> files;
	common::CollectFiles(exts, "..\\path", files);

	MySQLConnection sqlCon;
	if (!sqlCon.Connect("localhost", 3306, "navi", "1111", "navi"))
	{
		std::cout << "DB Connection Error\n";
		return 0;
	}

	set<string> chkInsertData;

	for (auto &fileName : files)
	{
		if (chkInsertData.end() != chkInsertData.find(fileName))
			continue; // already insert

		cPath file0;
		const uint64 dateTime = file0.GetTimeId(fileName);
		if (dateTime == 0)
			continue; // error occurred!

		// format : yyyy-mm-dd
		const string dateStr1 = cDateTime2::GetTimeStr4(dateTime).c_str();
		// format : yyyymmdd
		const string dateStr2 = cDateTime2::GetTimeStr5(dateTime).c_str();

		// find all same date data (by time_id)
		vector<sPathFile> pathFiles;
		for (auto &fn : files)
		{
			cPath file;
			const uint64 timeId = file.GetTimeId(fn);
			if (dateStr1 == cDateTime2::GetTimeStr4(timeId).c_str())
			{
				cPath file;
				const uint64 timeId = file.GetTimeId(fn);
				if (timeId == 0)
					continue; // error occurred!
				pathFiles.push_back({ timeId, fn });
			}
		}
		if (pathFiles.empty())
			continue; // error occurred!

		// sorting by timeId
		std::sort(pathFiles.begin(), pathFiles.end(),
			[](auto &a1, auto &a2) {return a1.timeId < a2.timeId; });

		const uint64 journeyTimeId = pathFiles[0].timeId;

		double totDistance = 0; // total journey distance
		double journeyTime = 0; // total journey time

		// calc all same date data
		for (auto &pathFile : pathFiles)
		{
			cPath path;
			if (!path.Read(pathFile.fileName))
				continue;

			for (uint i = 1; i < path.m_table.size(); ++i)
			{
				cPath::sRow &r0 = path.m_table[i - 1];
				cPath::sRow &r1 = path.m_table[i];
				const double d = WGS84Distance(r0.lonLat, r1.lonLat);
				if (d > 500)
					continue; // maybe lon/lat data crack
				totDistance += d;
			}

			if (!path.m_table.empty())
			{
				cPath::sRow &first = path.m_table.front();
				cPath::sRow &last = path.m_table.back();
				cDateTime2 dt = cDateTime2(last.dateTime) - cDateTime2(first.dateTime);
				journeyTime += (double)dt.m_t;
			}

			chkInsertData.insert(pathFile.fileName);
		}

		std::cout << "upload " << dateStr1 << "\n";

		const string sql =
			common::format("INSERT INTO journey_date (date, user_id, time_id, distance, journey_time)"
				" VALUES ('%s', '%d', '%I64u', '%f', '%f');"
				, dateStr1.c_str(), userId, journeyTimeId, totDistance, journeyTime);

		MySQLQuery query(&sqlCon, sql);
		query.ExecuteInsert();
	}

	std::cout << "finish~\n";
	return 1;
}



bool QueryInsertPathData(MySQLConnection &sqlCon, cPath::sRow &row
	, const unsigned __int64 journeyTimeId)
{
	const float speed = 0.f;
	const float altitude = 0.f;

	cDateTime2 dateTime;
	Str32 strDateTime = dateTime.GetTimeStr3(row.dateTime);

	const string sql =
		common::format("INSERT INTO path (date_time, user_id, journey_time_id, lon, lat, speed, altitude)"
			" VALUES ('%s', '%d', '%I64u', '%f', '%f', '%f', '%f');"
			, strDateTime.c_str()
			, userId, journeyTimeId, row.lonLat.x, row.lonLat.y, speed, altitude);

	MySQLQuery query(&sqlCon, sql);
	query.ExecuteInsert();
	return true;
}


// return distance lonLat0 - lonLat2
// http://www.movable-type.co.uk/scripts/latlong.html
double WGS84Distance(const Vector2d &lonLat0, const Vector2d &lonLat1)
{
	const double r = 6371000.f;
	const double lat0 = ANGLE2RAD2(lonLat0.y);
	const double lat1 = ANGLE2RAD2(lonLat1.y);
	const double dlat = ANGLE2RAD2(abs(lonLat0.y - lonLat1.y));
	const double dlon = ANGLE2RAD2(abs(lonLat0.x - lonLat1.x));

	const double a = sin(dlat / 2.f) * sin(dlat / 2.f)
		+ cos(lat0) * cos(lat1) * sin(dlon / 2.f) * sin(dlon / 2.f);
	const double c = 2 * atan2(sqrt(a), sqrt(1.f - a));
	const double d = r * c;
	return d;
}
