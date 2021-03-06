
#include "stdafx.h"
#include "path.h"


cPath::cPath(const StrPath &fileName //= ""
)
{
	if (!fileName.empty())
		Read(fileName);
}

cPath::~cPath()
{
	Clear();
}


bool Check6Val(const double val)
{
	uint64 c = (uint64)(val * 1000000.f);
	uint64 d = c % 1000;
	if (d == 0)
		return false;
	return true;
}


bool cPath::Read(const StrPath &fileName)
{
	cSimpleData simData(fileName);
	if (!simData.IsLoad())
		return false;

	Clear();

	m_table.reserve(simData.m_table.size());

	Vector2d p0;
	for (auto &row : simData.m_table)
	{
		if (row.size() < 3)
			continue;

		sRow info;
		info.dateTime = common::GetCurrentDateTime6(row[0]);
		info.lonLat.x = atof(row[1].c_str());
		info.lonLat.y = atof(row[2].c_str());

		// 전 좌표와 너무 큰차이가 나면 무시한다. (위경도로 계산)
		if (!p0.IsEmpty() && (p0.Distance(info.lonLat) > 0.08f))
			continue;
		if (!Check6Val(info.lonLat.x))
			continue;
		if (!Check6Val(info.lonLat.y))
			continue;

		p0 = info.lonLat;
		m_table.push_back(info);
	}

	return true;
}


// get path time id
// return first path time
uint64 cPath::GetTimeId(const StrPath &fileName)
{
	cSimpleData simData;
	if (!simData.ReadLine(fileName, ",", 1))
		return 0;
	if (simData.m_table[0].empty())
		return 0; // error occurred!
	const uint64 timeId = common::GetCurrentDateTime6(simData.m_table[0][0]);
	return timeId;
}


bool cPath::IsLoad() const
{
	return !m_table.empty();
}


void cPath::Clear()
{
	m_table.clear();
}
