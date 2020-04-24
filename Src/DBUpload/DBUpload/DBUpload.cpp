
#include "stdafx.h"
#include "path.h"

#pragma comment( lib, "libmysql.lib" )

int InsertPathData();
int InsertJourneyData();


int main()
{
    std::cout << "navi DB Upload!\n"; 
	InsertPathData();
	//InsertJourneyData();

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
					, strDateTime.c_str(), 1, row.dateTime, 0, 0);

			MySQLQuery query(&sqlCon, sql);
			query.ExecuteInsert();
			break;
		}
	}

	std::cout << "finish~\n";
	return 1;
}
