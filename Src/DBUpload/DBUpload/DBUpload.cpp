
#include "stdafx.h"
#include "path.h"

#pragma comment( lib, "libmysql.lib" )


int main()
{
    std::cout << "Hello World!\n"; 

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

		std::cout << fileName << " upload\n";
		for (auto &row : path.m_table)
		{
			const float speed = 0.f;
			const float altitude = 0.f;
			
			cDateTime2 dateTime;
			Str32 strDateTime = dateTime.GetTimeStr3(row.dateTime);

			string sql =
				common::format("INSERT INTO path (date_time, user_name, lon, lat, speed, altitude)"
				" VALUES ('%s', '%s', '%f', '%f', '%f', '%f');"
				, strDateTime.c_str()
				, "jjuiddong", row.lonLat.x, row.lonLat.y, speed, altitude);

			MySQLQuery query(&sqlCon, sql);
			query.ExecuteInsert();
		}
	}

	std::cout << "finish~\n";

	return 0;
}

