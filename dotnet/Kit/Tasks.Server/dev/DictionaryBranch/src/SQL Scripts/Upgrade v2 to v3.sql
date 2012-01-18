/*
 * Copyright 2004 - $Date: 2008-11-15 23:58:07 +0100 (za, 15 nov 2008) $ by PeopleWare n.v..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

print '';
print '';
print 'Upgrading from v2 to v3 ...';
print '';


print 'Creating table dbo.TaskAttributes';
create table dbo.TaskAttributes (
  TaskID bigint not null,
  AttributeName varchar(64) not null,
  AttributeValue varchar(256) null,

  constraint PK_TaskAttributes primary key (TaskID, AttributeName),
  constraint FK_TaskAttributes_Task foreign key (TaskID) references dbo.Task (TaskId)
 );
go
create index IX_TaskAttributes_Attribute on dbo.TaskAttributes(AttributeValue, AttributeName);
go

print 'Converting dbo.Task.Reference into records of dbo.TaskAttributes'

delete from dbo.TaskAttributes

insert into dbo.TaskAttributes
select 
	att.TaskID, att.AttributeName, 
--	att.Reference, att.TaskType,
	nullif(att.AttributeValue, '') as AttributeValue
from
(
	-- common attributes for all tasks
	select
		t.TaskID, 'TypeName' as AttributeName,
--		t.Reference, t.TaskType,
		dbo.fnRegexGroup(
			t.Reference, 
			N'(?<TypeName>/[a-zA-Z]*/[a-zA-Z]*/[a-zA-Z]*)/.*', 
			N'TypeName') 
		as AttributeValue
	from
		Task t
		
	union all
	
	select
		t.TaskID, 'RetirementPlanName' as AttributeName,
--		t.Reference, t.TaskType,
		dbo.fnRegexGroup(
			t.Reference, 
			N'/[a-zA-Z]*/[a-zA-Z]*/[a-zA-Z]*/(?<RetirementPlanName>[a-zA-Z]*)/.*', 
			N'RetirementPlanName') 
		as AttributeValue
	from
		Task t
		
	union all
	
	select
		t.TaskID, 'AffiliateSynergyID' as AttributeName,
--		t.Reference, t.TaskType,
		dbo.fnRegexGroup(
			t.Reference, 
			N'/[a-zA-Z]*/[a-zA-Z]*/[a-zA-Z]*/[a-zA-Z]*/(?<AffiliateSynergyID>[0-9]*)@.*', 
			N'AffiliateSynergyID') 
		as AttributeValue
	from
		Task t
		
	union all
	
	-- for TaskType Affiliation (or for Document with a RefType Affiliation), get AffilationID
	select
		t.TaskID, 'AffiliationID' as AttributeName,
--		t.Reference, t.TaskType,
		dbo.fnRegexGroup(
			t.Reference, 
			N'/[a-zA-Z]*/[a-zA-Z]*/[a-zA-Z]*/[a-zA-Z]*/[0-9]*@(?<AffiliationID>[0-9]*)/.*', 
			N'AffiliationID') 
		as AttributeValue
	from
		Task t
	where 
		t.TaskType like '/PensioB/Sempera/Affiliation/%' or
		(t.TaskType like '/Constructiv/Documents/%' and t.Reference like '/PensioB/Sempera/Affiliation/%')
		
	union all

	-- for TaskType Payments (or for Document with a RefType Payments), get PaymentDossierID
	select
		t.TaskID, 'PaymentDossierID' as AttributeName,
--		t.Reference, t.TaskType,
		dbo.fnRegexGroup(
			t.Reference, 
			N'/[a-zA-Z]*/[a-zA-Z]*/[a-zA-Z]*/[a-zA-Z]*/[0-9]*@(?<PaymentDossierID>[0-9]*)/.*', 
			N'PaymentDossierID')
		as AttributeValue
	from
		Task t
	where 
		t.TaskType like '/PensioB/Sempera/Payments/%' or
		(t.TaskType like '/Constructiv/Documents/%' and t.Reference like '/PensioB/Sempera/Payments/%')
		
	union all
	
	-- for TaskType Payments (or for Document with a RefType Payments), also try to get BeneficiarySynergyID and BeneficiaryID
	select
		t.TaskID, 'BeneficiarySynergyID' as AttributeName,
--		t.Reference, t.TaskType,
		dbo.fnRegexGroup(
			t.Reference, 
			N'/[a-zA-Z]*/[a-zA-Z]*/[a-zA-Z]*/[a-zA-Z]*/[0-9]*@[0-9]*/(?<BeneficiarySynergyID>[0-9]*)@[0-9]*/.*', 
			N'BeneficiarySynergyID') 
		as AttributeValue
	from
		Task t
	where 
		t.TaskType like '/PensioB/Sempera/Payments/%' or
		(t.TaskType like '/Constructiv/Documents/%' and t.Reference like '/PensioB/Sempera/Payments/%')

	union all
	
	select
		t.TaskID, 'BeneficiaryID' as AttributeName,
--		t.Reference, t.TaskType,
		dbo.fnRegexGroup(
			t.Reference, 
			N'/[a-zA-Z]*/[a-zA-Z]*/[a-zA-Z]*/[a-zA-Z]*/[0-9]*@[0-9]*/[0-9]*@(?<BeneficiaryID>[0-9]*)/.*', 
			N'BeneficiaryID') 
		as AttributeValue
	from
		Task t
	where 
		t.TaskType like '/PensioB/Sempera/Payments/%' or
		(t.TaskType like '/Constructiv/Documents/%' and t.Reference like '/PensioB/Sempera/Payments/%')
		
	union all
	
	-- for TaskType Documents, get DocumentMetaDataID
	-- Note: the DocumentMetaDataID attribute is required, so NULL is forced to come through by temporary converting it to ''
	select
		t.TaskID, 'DocumentMetaDataID' as AttributeName,
--		t.Reference, t.TaskType,
		isnull(
			dbo.fnRegexGroup(
				t.Reference, 
				N'/[a-zA-Z]*/[a-zA-Z]*/[a-zA-Z]*/[a-zA-Z]*/[0-9]*@[0-9]*/([0-9]*@[0-9]*/)?(?<DocumentMetaDataID>[0-9]*)/$', 
				N'DocumentMetaDataID')
			, '')
		as AttributeValue
	from
		Task t
	where 
		t.TaskType like '/Constructiv/Documents/%'
		

) att
where
	att.AttributeValue is not NULL

go

if exists
(
select
	tatn.AttributeValue + '/' + tapn.AttributeValue + '/' + taas.AttributeValue + '@' +
	isnull(taai.AttributeValue, '') + isnull(tapd.AttributeValue, '') + '/' +
	isnull(tabs.AttributeValue + '@', '') + isnull(tabi.AttributeValue + '/', '') +
	isnull(tado.AttributeValue + '/', ''),
	t.*
from
	dbo.Task t
	join dbo.TaskAttributes tatn on tatn.TaskID = t.TaskID and tatn.AttributeName = 'TypeName'
	join dbo.TaskAttributes tapn on tapn.TaskID = t.TaskID and tapn.AttributeName = 'RetirementPlanName'
	join dbo.TaskAttributes taas on taas.TaskID = t.TaskID and taas.AttributeName = 'AffiliateSynergyID'
	left outer join dbo.TaskAttributes taai on taai.TaskID = t.TaskID and taai.AttributeName = 'AffiliationID'
	left outer join dbo.TaskAttributes tapd on tapd.TaskID = t.TaskID and tapd.AttributeName = 'PaymentDossierID'
	left outer join dbo.TaskAttributes tabs on tabs.TaskID = t.TaskID and tabs.AttributeName = 'BeneficiarySynergyID'
	left outer join dbo.TaskAttributes tabi on tabi.TaskID = t.TaskID and tabi.AttributeName = 'BeneficiaryID'
	left outer join dbo.TaskAttributes tado on tado.TaskID = t.TaskID and tado.AttributeName = 'DocumentMetaDataID'
where
	tatn.AttributeValue + '/' + tapn.AttributeValue + '/' + taas.AttributeValue + '@' +
	isnull(taai.AttributeValue, '') + isnull(tapd.AttributeValue, '') + '/' +
	isnull(tabs.AttributeValue + '@', '') + isnull(tabi.AttributeValue + '/', '') +
	isnull(tado.AttributeValue + '/', '')
	<> t.Reference
)
begin
	print 'ERROR: dbo.TaskAttributes is not consistent with dbo.Task.Reference !!!'
end

go
	