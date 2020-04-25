
#include "stdafx.h"
#include "path.h"

#pragma comment( lib, "libmysql.lib" )

int InsertPathData();
int InsertJourneyData();
double WGS84Distance(const Vector2d &lonLat0, const Vector2d &lonLat1);


int main()
{
    std::cout << "navi DB Upload!\n"; 
	//InsertPathData();
	InsertJourneyData();

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

	int cnt = 0;
	for (auto &fileName : files)
	{
		cPath path;
		if (!path.Read(fileName))
			continue;

		std::cout << "upload " << fileName << "\n";
		const unsigned __int64 journeyTimeId = path.m_table.empty() ? 0 : path.m_table[0].dateTime;

		for (auto &row : path.m_table)
		{
			const float speed = 0.f;
			const float altitude = 0.f;

			cDateTime2 dateTime;
			Str32 strDateTime = dateTime.GetTimeStr3(row.dateTime);

			string sql =
				common::format("INSERT INTO path (date_time, user_id, journey_time_id, lon, lat, speed, altitude)"
					" VALUES ('%s', '%d', '%I64u', '%f', '%f', '%f', '%f');"
					, strDateTime.c_str()
					, 1, journeyTimeId, row.lonLat.x, row.lonLat.y, speed, altitude);

			MySQLQuery query(&sqlCon, sql);
			query.ExecuteInsert();
		}
	}

	std::cout << "finish~\n";
	return 1;
}


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

	int cnt = 0;
	for (auto &fileName : files)
	{
		cPath path;
		if (!path.Read(fileName))
			continue;

		double totDistance = 0; // total journey distance
		for (uint i = 1; i < path.m_table.size(); ++i)
		{
			cPath::sRow r0 = path.m_table[i - 1];
			cPath::sRow r1 = path.m_table[i];
			const double d = WGS84Distance(r0.lonLat, r1.lonLat);
			totDistance += d;
		}

		std::cout << "upload " << fileName << "\n";
		for (auto &row : path.m_table)
		{
			const float speed = 0.f;
			const float altitude = 0.f;

			cDateTime2 dateTime;
			Str32 strDateTime = dateTime.GetTimeStr3(row.dateTime);

			string sql =
				common::format("INSERT INTO journey_date (date, user_id, time_id, distance, journey_time)"
					" VALUES ('%s', '%d', '%I64u', '%f', '%f');"
					, strDateTime.c_str(), 1, row.dateTime, totDistance, 0);

			MySQLQuery query(&sqlCon, sql);
			query.ExecuteInsert();
			break;
		}
	}

	std::cout << "finish~\n";
	return 1;
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
