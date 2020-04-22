
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
	cSimpleData simData(fileName.c_str());
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

		// �� ��ǥ�� �ʹ� ū���̰� ���� �����Ѵ�. (���浵�� ���)
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


bool cPath::IsLoad() const
{
	return !m_table.empty();
}


void cPath::Clear()
{
	m_table.clear();
}
