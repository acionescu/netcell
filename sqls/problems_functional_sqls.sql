select * from users

--create user
insert into users(username,password) values('first_user','I am number 1')
insert into users(username,password) values('second_user','I am number 2')
insert into users(username,password) values('third_user','I am number 3')

--login
select * from users where username='first_user' and password='I am number 1'

select * from users

--create an entity
select * from entities
select * from complex_entity_type  

delete from entities where id in(22,24)
--first create a group
/*
in the real world all the parameters will be variable 
*/
insert into entities(creator_id,entity_type_id,complex_entity_type_id,parent_entity_id,title,content)
values(
(select id from users where username='first_user')
,(select id from entity_types  where type='GROUP')
,(select id from complex_entity_type  where complex_type ='/GROUP')
,(select id from entities where id = parent_entity_id)
,'First Group'
,'This group is the first group created to test the create entity sql'
)

--now create the metagroup to handle application issues

insert into entities(creator_id,entity_type_id,complex_entity_type_id,parent_entity_id,title,content)
values(
(select id from users where username='God')
,(select id from entity_types  where type='METAGROUP')
,(select id from complex_entity_type  where complex_type ='/METAGROUP')
,(select id from entities where id = parent_entity_id)
,'Default Group'
,'The holder for all the discussed issues'
)

--insert an issue
insert into entities(creator_id,entity_type_id,complex_entity_type_id,parent_entity_id,title,content)
values(
(select id from users where username='first_user')
,(select id from entity_types  where type='ISSUE')
,(select id from complex_entity_type  where complex_type ='/METAGROUP/ISSUE')
,(select id from entities where entity_type_id=7)
,'Issue 2'
,'Another test issue'
)

insert into entities(creator_id,entity_type_id,complex_entity_type_id,parent_entity_id,title,content)
values(
(select id from users where username='first_user')
,(select id from entity_types  where type='ISSUE')
,(select id from complex_entity_type  where complex_type ='/METAGROUP/ISSUE')
,(select id from entities where entity_type_id=7)
,'Issue 3'
,'Let''s just freak out about our condition and enter into a deep depression.'
)

--insert a comment
insert into entities(creator_id,entity_type_id,complex_entity_type_id,parent_entity_id,title,content)
values(
(select id from users where username='first_user')
,(select id from entity_types  where type='COMMENT')
,(select id from complex_entity_type  where complex_type ='/METAGROUP/ISSUE/COMMENT')
,(select id from entities where title='Issue 2')
,'Comment 1'
,'This is a test comment.Do you like it?'
)

--insert a solution
insert into entities(creator_id,entity_type_id,complex_entity_type_id,parent_entity_id,title,content)
values(
(select id from users where username='first_user')
,(select id from entity_types  where type='SOLUTION')
,(select id from complex_entity_type  where complex_type ='/METAGROUP/ISSUE/SOLUTION')
,(select id from entities where title='Issue 2')
,'Solution 2'
,'This is a another possible solution do this issue. What do you think?'
)

-----------------------------------------------------------------------------
--create user
insert into users(username,password,email,user_hash) values('$username','$password','$email',md5('$username'||'$password'||'$email'||localtimestamp))

select * from users

--update user
update users
#if($userId)
set id=$userId
#else
set user_hash='$userHash'
#end
#if($password)
, password='$password'
#end
#if($email)
,email='$email'
#end
#if($userId)
where id=$userId;
--update user hash
update users
set user_hash=compute_user_hash($userId)
where id=$userId;
#else
where user_hash='$userHash';
--update user hash
update users
set user_hash=compute_user_hash((select id from users where user_hash='$userHash'))
where user_hash='$userHash';
#end



--insert a generic entity
insert into entities(creator_id,entity_type_id,complex_entity_type_id,parent_entity_id,title,content)
values(
$userId
,(select id from entity_types  where type='$entityType')
,(select id from complex_entity_type  where complex_type ='$complexType')
,$parentId
,'$title'
,'$content'
)


select * from entities_votes
--vote issue
insert into entities_votes(entity_id,user_id,vote)
values(
(select id from entities where title='Issue 3')
,(select id from users where username='third_user')
,'n'
)
--get application config parameters
select * from application_config

--get complex entity types
select * from complex_entity_type


--select popular issues

select id from entities, entities_votes ev
where entity_type_id=(select id from entity_types where type='ISSUE')
and id = ev.entity_id
and ev.vote='y'
and (select count(a.vote) from entities_votes a where a.entity_id=id and a.vote='n')*1.0
/ (select count(a.vote) from entities_votes a where a.entity_id=id and a.vote='y')*1.0 < (select number_value from application_config where param_name='votes_ratio_for_most_popular_issue')
group by id
order by count(ev.vote) desc
limit $itemsOnPage offset $itemsOnPage * ($pageNumber - 1)

--most popular issues count
select count(distinct(id)) from entities, entities_votes ev
where entity_type_id=(select id from entity_types where type='ISSUE')
and id = ev.entity_id
and ev.vote='y'
and (select count(a.vote) from entities_votes a where a.entity_id=id and a.vote='n')*1.0
/ (select count(a.vote) from entities_votes a where a.entity_id=id and a.vote='y')*1.0 < (select number_value from application_config where param_name='votes_ratio_for_most_popular_issue')


--select newest issues
select id from entities
where entity_type_id=(select id from entity_types where type='ISSUE')
order by insert_date desc
limit $itemsOnPage offset $itemsOnPage*($pageNumber-1)

--newest issues count
select count(id) from entities
where entity_type_id=(select id from entity_types where type='ISSUE')


--issues summary

select 
i.id
,i.title
,i.insert_date
/*
,sum(case evp.vote
	when 'y' then 1
	else 0
	end) pro_votes
,sum(case evp.vote
	when 'n' then 1
	else 0
	end) opposed_votes
	*/
,votes.pro_votes
,votes.opposed_votes	
,votes.total_votes
, case (votes.total_votes)
when 0 then 0
else ((votes.pro_votes-votes.opposed_votes) * 1.0)/((votes.total_votes)*1.0) 
end popularity_index
,count(distinct(s.id)) solutions	
,count(distinct(c.id)) comments
,max(c.insert_date) last_comment_insert_date	
from entities i
--left outer join entities_votes evp on (i.id=evp.entity_id )
left outer join entities c on (i.id = c.parent_entity_id and c.entity_type_id=(select id from entity_types where type='COMMENT'))
left outer join entities s on (i.id = s.parent_entity_id and s.entity_type_id=(select id from entity_types where type='SOLUTION'))
left outer join (
select 
e.id entity_id
,sum(case v.vote
	when 'y' then 1
	else 0
	end) pro_votes
,sum(case v.vote
	when 'n' then 1
	else 0
	end) opposed_votes
,count(v.vote) total_votes	
from entities e
left outer join entities_votes v on (e.id = v.entity_id)
group by e.id	
) votes on (i.id = votes.entity_id)
where
i.entity_type_id=(select id from entity_types where type='ISSUE')
--and i.id in ( $idsList )
--and i.id in (27,26)
group by i.id,i.title,i.insert_date,votes.pro_votes,votes.opposed_votes,votes.total_votes
order by popularity_index desc



--get possible actions for selected entity and user type
select 
a.action
,a.name action_name
,a.params action_params
,at.type action_type
, atg.target action_target
, ut.type user_type
, et.type target_entity_type 
from action_strategies acts
,actions a
,action_types at
,user_types ut
,action_targets atg
,entity_types et
where
(
complex_entity_type_id = ( select complex_entity_type_id from entities where id=27 )
--or
--complex_entity_type_id = (select id from complex_entity_type  where complex_type in ('*'))
)
and user_type_id in(
(select id from user_types  where type in('*','GUEST'))

)
and acts.action_id=a.id
and a.action_type_id=at.id
and acts.user_type_id = ut.id
and acts.action_target_id=atg.id
and acts.entity_type_id=et.id
order by acts.complex_entity_type_id,acts.order_index

--
select 
a.action
,a.name action_name
,a.params action_params
,at.type action_type
, atg.target action_target
, ut.type user_type
, et.type target_entity_type 
from action_strategies acts
,actions a
,action_types at
,user_types ut
,action_targets atg
,entity_types et
where
(
complex_entity_type_id = ( select complex_entity_type_id from entities where id= $entityId )
or
complex_entity_type_id = (select id from complex_entity_type  where complex_type in ('*'))
)
and user_type_id in(
(select id from user_types  where type in('*','$userTypes'))
)
and acts.action_id=a.id
and a.action_type_id=at.id
and acts.user_type_id = ut.id
and acts.action_target_id=atg.id
and acts.entity_type_id=et.id
order by acts.complex_entity_type_id,acts.order_index
 
--generic entity display

select * from entities

WITH RECURSIVE subentities_list(id, complex_type_id, parent_entity_id, depth) AS (
        SELECT e.id, e.complex_entity_type_id, e.parent_entity_id, 0
        FROM entities e
        where e.parent_entity_id=$entityId
                
      UNION ALL
        SELECT e.id, e.complex_entity_type_id, e.parent_entity_id, eg.depth + 1
        FROM entities e, subentities_list eg
        WHERE eg.id = e.parent_entity_id
)
select e.id, e.title,e.content,e.insert_date,cet.complex_type 
,e.creator_id
,votes.pro_votes
,votes.opposed_votes	
,votes.total_votes
, case (votes.total_votes)
when 0 then 0
else (votes.pro_votes-votes.opposed_votes) 
end popularity_index
,ep.priority general_priority
,gs.status general_status
#if($userId)
,eu.vote user_vote
,eu.last_vote_update
,eu.priority
,eu.last_priority_update
,st.status
,eu.last_status_update
#end
#foreach( $entityContext in $subtypesList.getValues())
,(select count(*) from subentities_list where complex_type_id = $entityContext.getValue("id") and depth=0) $entityContext.getValue("simple_subtype")_subtype_count
,(select count(*) from subentities_list where complex_type_id = $entityContext.getValue("id")) $entityContext.getValue("simple_subtype")_recursive_subtype_count
#end	
, $subtypesList.size() subtypes_count
from entities e
left outer join (
select 
e2.id entity_id
,sum(case v.vote
	when 'y' then 1
	else 0
	end) pro_votes
,sum(case v.vote
	when 'n' then 1
	else 0
	end) opposed_votes
,count(v.vote) total_votes	
from entities e2
left outer join entities_users v on (e2.id = v.entity_id)
group by e2.id	
) votes on (e.id = votes.entity_id)
#if($userId)
left outer join entities_users eu on(e.id = eu.entity_id and eu.user_id=$userId)
left outer join statuses st on(eu.status_id=st.id)
#end
left outer join entities_priorities ep on (e.id=ep.entity_id)
left outer join entities_statuses es on (e.id=es.entity_id)
left outer join statuses gs on(es.status_id=gs.id)
inner join complex_entity_type cet on (e.complex_entity_type_id = cet.id)
where e.id=$entityId
group by e.id, e.title, e.content, e.insert_date, cet.complex_type,e.creator_id
,votes.pro_votes,votes.opposed_votes,votes.total_votes,ep.priority,gs.status
#if($userId)
,eu.vote,eu.last_vote_update,eu.priority,eu.last_priority_update
,st.status,eu.last_status_update
#end

--get first level subentities for a given entity id

select cet.id, cet.complex_type as complex_subtype
,substring(cet.complex_type from (length(cet1.complex_type||'/')+1)) as simple_subtype
from
entities e,
complex_entity_type cet1,
complex_entity_type cet
where cet.complex_type like (cet1.complex_type||'/%') 
and strpos(substring(cet.complex_type from length(cet1.complex_type||'/')+1),'/') = 0
and e.id = 26--$entityId
and e.complex_entity_type_id = cet1.id
order by cet.id

--get first level subentities for a given complex entity type
select 
ca.id
,ca.complex_type complex_subtype
, ca.complex_array[array_length(ca.complex_array,1)] simple_subtype
from 
(
select c.id, c.complex_type, string_to_array(c.complex_type,'/') complex_array from complex_entity_type c
where c.complex_type like ('$complexType'||'/%') 
) ca
where array_length(ca.complex_array,1) = (array_length(string_to_array('$complexType','/'),1)+1)
order by ca.id

--get subtypes for entity id
select 
cet2.id
,cet2.complex_type complex_subtype
,cet2.complex_type simple_subtype 
from 
entities e
, complex_entity_type cet
, entity_types_relations ctr
, complex_entity_type cet2
where 
e.complex_entity_type_id=cet.id
and e.complex_entity_type_id = ctr.source_entity_type_id
and ctr.target_entity_type_id = cet2.id
and e.id=$entityId

--get subtypes for complex type

select 
cet2.id
,cet2.complex_type complex_subtype
,cet2.complex_type simple_subtype 
from 
complex_entity_type cet
, entity_types_relations ctr
, complex_entity_type cet2
where 
cet.id = ctr.source_entity_type_id
and ctr.target_entity_type_id = cet2.id
and cet.complex_type='$complexType'

-- get subtypes for all complex types
select 
cet.complex_type source_complex_type
,cet2.complex_type complex_subtype
,cet2.complex_type simple_subtype 
from 
complex_entity_type cet
, entity_types_relations ctr
, complex_entity_type cet2
where 
cet.id = ctr.source_entity_type_id
and ctr.target_entity_type_id = cet2.id


-- list entities

#if($parentId)
WITH RECURSIVE subentities_list(id, complex_type_id,complex_type, parent_entity_id, depth) AS (
        SELECT e.id, cet.id complex_type_id,cet.complex_type, e.parent_entity_id, 0
        FROM entities e, complex_entity_type cet
        where e.parent_entity_id=$parentId
        and e.complex_entity_type_id=cet.id
        
      UNION ALL
        SELECT e.id, cet.id complex_type_id,cet.complex_type, e.parent_entity_id, eg.depth + 1
        FROM entities e, subentities_list eg, complex_entity_type cet
        WHERE eg.id = e.parent_entity_id
        and e.complex_entity_type_id=cet.id
        #if($depth)
        and eg.depth &lt; $depth
        #end
)
#elseif($entityId)
WITH RECURSIVE entity_upstream_hierarchy(id, complex_type, parent_entity_id, depth) AS (
        SELECT e.id, cet.complex_type, e.parent_entity_id, 0
        FROM entities e, complex_entity_type cet
        where e.id=$entityId
        and e.complex_entity_type_id=cet.id
        
      UNION ALL
        SELECT e.id, cet.complex_type, e.parent_entity_id, eg.depth + 1
        FROM entities e, entity_upstream_hierarchy eg, complex_entity_type cet
        WHERE e.id = eg.parent_entity_id
	and cet.complex_type in ('ISSUE','SOLUTION')
        and e.complex_entity_type_id=cet.id
       #if($depth)
        and eg.depth &lt; $depth
       #end
)
#end
select 
e.id
, e.title
#if($withContent)
, e.content
#else
, substring(e.content from 1 for 1000) content_preview
#end
, e.insert_date
, cet.complex_type 
, e.creator_id
, idsList.depth
,votes.pro_votes
,votes.opposed_votes	
,votes.total_votes
, case (votes.total_votes)
when 0 then 0
else (votes.pro_votes-votes.opposed_votes)
end popularity_index
,ep.priority general_priority
,gs.status general_status
#if($userId)
,eu.vote user_vote
,eu.last_vote_update
,eu.priority
,eu.last_priority_update
,st.status
,eu.last_status_update
#end
#if($subtypesList)
, $subtypesList.size() subtypes_count
#foreach( $entityContext in $subtypesList.getValues())
#set($currentTable = "e${velocityCount}.id")
,count(distinct($currentTable)) $entityContext.getValue("simple_subtype")_subtype_count
#end	
#end
--if is a recent activity list
#if($parentId &amp;&amp; !$entityType &amp;&amp; !$userActionColumn)
-- bring the action type
,actLog.action_type_id
,(select type from action_types where id=actLog.action_type_id) action_type
-- bring also the parent id and title
,e.parent_entity_id
,(select title from entities where id=e.parent_entity_id) parent_title
#end
from entities e
left outer join (
select 
e2.id entity_id
,sum(case v.vote
	when 'y' then 1
	else 0
	end) pro_votes
,sum(case v.vote
	when 'n' then 1
	else 0
	end) opposed_votes
,count(v.vote) total_votes	
from entities e2
left outer join entities_users v on (e2.id = v.entity_id)
group by e2.id	
) votes on (e.id = votes.entity_id)
#if($subtypesList)
#foreach( $entityContext in $subtypesList.getValues())
#set($currentTable = "e${velocityCount}")
left outer join entities $currentTable on (e.id = ${currentTable}.parent_entity_id and ${currentTable}.complex_entity_type_id=$entityContext.getValue("id"))
#end
#end
#if($userId)
left outer join entities_users eu on(e.id = eu.entity_id and eu.user_id=$userId)
left outer join statuses st on(eu.status_id = st.id)
#end
left outer join entities_priorities ep on (e.id=ep.entity_id)
left outer join entities_statuses es on (e.id=es.entity_id)
left outer join statuses gs on(es.status_id=gs.id)
inner join complex_entity_type cet on (e.complex_entity_type_id = cet.id)
#if($parentId)
#if($entityType)
join subentities_list idsList on (e.id = idsList.id and idsList.complex_type='$entityType')
#elseif($complexTypesIds)
join subentities_list idsList on (e.id = idsList.id and idsList.complex_type_id in $complexTypeIds)
#else
join subentities_list idsList on (e.id = idsList.id)
#if(!$userActionColumn)
join activity_log actLog on (e.id = actLog.entity_id)
#end
#end
#elseif($entityId)
join entity_upstream_hierarchy idsList on (e.id = idsList.id )
#end
where 1=1
#if($searchString)
and lower(e.title) like '%$searchString%' or lower(e.content) like '%$searchString%'
#end
#if($tag)
and exists (select 1 from entities_tags et,tags t where et.entity_id=e.id and et.tag_id=t.id and t.tag='$tag')
#end
#if($status)
and st.status='$status'
#end
#if($globalStatus)
and gs.status='$globalStatus'
#end
#if($userActionColumn)
and exists (select 1 from entities_users where entity_id=e.id #if($filterByUser)and user_id=$userId#end and $userActionColumn is not null)
#end
group by 
e.id
, e.title
#if($withContent)
, e.content
#else
,content_preview
#end
, e.insert_date
, e.last_update
, cet.complex_type
, e.creator_id
, idsList.depth
,votes.pro_votes,votes.opposed_votes,votes.total_votes
,ep.priority,gs.status
#if($userId)
,eu.vote
,eu.last_vote_update
,eu.priority
,eu.last_priority_update
,st.status
,eu.last_status_update
#end
#if($parentId &amp;&amp; !$entityType &amp;&amp; !$userActionColumn)
,e.parent_entity_id
,actLog.action_type_id
,actLog.action_timestamp
#end
order by $sortList
limit $itemsOnPage offset $itemsOnPage * ( $pageNumber - 1 )


--entities list count

WITH RECURSIVE subentities_list(id, complex_type_id,complex_type, parent_entity_id, depth) AS (
        SELECT e.id, cet.id complex_type_id,cet.complex_type, e.parent_entity_id, 0
        FROM entities e, complex_entity_type cet
        where e.parent_entity_id=$parentId
        and e.complex_entity_type_id=cet.id
        
      UNION ALL
        SELECT e.id, cet.id complex_type_id,cet.complex_type, e.parent_entity_id, eg.depth + 1
        FROM entities e, subentities_list eg, complex_entity_type cet
        WHERE eg.id = e.parent_entity_id
        and e.complex_entity_type_id=cet.id
        #if($depth)
        and eg.depth &lt; $depth
        #end
)
select 
#if($entityType || $userActionColumn)
count(distinct(e.id)) 
#else
count(*)
#end
from entities e
join subentities_list sl on (e.id=sl.id)
#if(!$entityType &amp;&amp; !$userActionColumn)
join activity_log al on (e.id=al.entity_id)
#end
where 1=1
#if($entityType)
and sl.complex_type='$entityType'
#end
#if($searchString)
and (lower(e.title) like '%$searchString%' or lower(e.content) like '%$searchString%')
#end
#if($tag)
and exists (select 1 from entities_tags et,tags t where et.entity_id=e.id and et.tag_id=t.id and t.tag='$tag')
#end
#if($status)
and exists (select 1 from entities_users eu,statuses s where eu.entity_id=e.id and eu.status_id=s.id and s.status='$status')
#end
#if($globalStatus)
and exists (select 1 from entities_statuses es,statuses s where entity_id=e.id and es.status_id=s.id and s.status='$globalStatus')
#end
#if($userActionColumn)
and exists (select 1 from entities_users where entity_id=e.id #if($filterByUser)and user_id=$userId#end and $userActionColumn is not null)
#end


--create an entity
insert into entities(id,creator_id,entity_type_id,complex_entity_type_id,parent_entity_id,title,content)
values(
$entityId
,$userId
,(select id from entity_types  where type='$entityType')
,(select id from complex_entity_type  where complex_type ='$complexType')
,$parentId
,'$title'
,'$content'
)

--get rowscount for table and column value
select count($column) from $table
where $column=$value

-- get ids for taglist
delete from tags

select * 
from
tags
where 
tag in
(
#foreach($t in $tagList.getValues() )
'$t'
#if(  ($tagList.size() - $velocityCount)  &gt; 0 ) 
,
#end
#end
)

-- insert tags that don't exist in the table
#foreach($t in $tagsList.getValues())
#if(!($tagsWithIds.containsValue("$t")))
insert into tags(tag) values('$t');
#end
#end

-- insert tags for entity
select * from entities_tags  
select * from tags

#foreach($row in $tagsIds.getValues())
insert into entities_tags(entity_id,tag_id) values($entityId,$row.getValue("id"));
#end

--get tags for entity id
select t.* from entities_tags et
,tags t
where et.entity_id=$entityId 
and et.tag_id=t.id

--vote
select * from entities_votes

insert into entities_votes(entity_id,user_id,vote) values($entityId,$userId,'$vote');

--change vote

update entities_votes 
set vote='$vote'
where entity_id=$entityId
and user_id=$userId

--remove vote
delete from entities_votes
where entity_id=$entityId
and user_id=$userId

--get user vote for entity
select vote, last_update 
from entities_votes
where entity_id=$entityId 
and user_id=$userId

--get all available actions
select 
cet.complex_type source_entity_complex_type
,a.action
,a.name action_name
,a.params action_params
,at.type action_type
, atg.target action_target
, ut.type user_type
, et.type target_entity_type 
, cet2.complex_type target_entity_complex_type
from action_strategies acts
,actions a
,action_types at
,user_types ut
,action_targets atg
,entity_types et
,complex_entity_type cet
,complex_entity_type cet2
where
acts.complex_entity_type_id=cet.id
and acts.action_id=a.id
and a.action_type_id=at.id
and acts.user_type_id = ut.id
and acts.action_target_id=atg.id
and acts.entity_type_id=et.id
and acts.target_entity_complex_type_id=cet2.id
order by acts.complex_entity_type_id,acts.order_index


--update entity
update entities
set title='$title'
,content='$content'
where id = $entityId


--get all complex types that can be a subtype
select 
id
, complex_type complex_subtype
, complex_type simple_subtype
from complex_entity_type
where is_subtype='y'
order by id


--get all entities relations
select 
cet.complex_type source_complex_type
,cet2.complex_type complex_subtype
from 
complex_entity_type cet
, entity_types_relations ctr
, complex_entity_type cet2
where 
cet.id = ctr.source_entity_type_id
and ctr.target_entity_type_id = cet2.id

--insert into activity_log
insert into activity_log(entity_id,action_type_id)
values($entityId,$actionTypeId)


--create user entity record
insert into entities_users(entity_id,user_id) values($entityId,$userId)

--get id for status
select * from statuses where status='$status'

--create status
insert into statuses(status) values('$status')

--update entities_users


update entities_users
#if($flag=="vote" )
#if($vote)
set vote='$vote'
#else
set vote=null
#end
#elseif($flag=="priority")
#if($priority )
set priority=$priority
#else
set priority=null
#end
#elseif($flag=="status")
#if($statusId != -1)
set status_id=$statusId
#else
set status_id=null
#end
#end
where entity_id=$entityId and user_id=$userId

--get tags by most used
select tag_id,t.tag,count(*) from entities_tags et,tags t
where et.tag_id=t.id
group by tag_id,t.tag
order by count desc


--get statuses most used
select s.id,s.status,count(*) from entities_users eu, statuses s
where eu.status_id=s.id
group by s.id,s.status
order by count desc


-- entities_priorities_count view 
select entity_id,priority,count(*)from entities_users eu
where priority is not null
group by entity_id,priority

--get general priority for entity
select distinct(entity_id),get_general_priority(entity_id) as priority from entities_users
where priority is not null;


--entities statuses count view
select entity_id,status_id,count(*) from entities_users eu
where status_id is not null
group by entity_id,status_id
order by count desc

--most used statuses view
 SELECT esc.entity_id, esc.status_id, esc.count
   FROM entities_statuses_count esc
  WHERE esc.count = (( SELECT max(entities_statuses_count.count) AS max
           FROM entities_statuses_count
          WHERE entities_statuses_count.entity_id = esc.entity_id))
	and esc.status_id = (select min(status_id) from entities_statuses_count 
	where entity_id=esc.entity_id and count=esc.count)


--insert user login info
insert into users_ips(ip,user_id) values('$ip',$userId);
update users 
set last_login=localtimestamp
where id=$userId;

--update user logout timestamp
update users
set last_logout=localtimestamp
where id=$userId;

--check email exists
select count(*) from users
where email='$email'
#if($userId)
and id != $userId
#end

select * from users
select * from users_ips

select * from actions
