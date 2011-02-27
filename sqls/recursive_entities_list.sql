WITH RECURSIVE subentities_list(id, complex_type, parent_entity_id, depth) AS (
        SELECT e.id, cet.complex_type, e.parent_entity_id, 0
        FROM entities e, complex_entity_type cet
        where e.id=221
        and e.complex_entity_type_id=cet.id
        
      UNION ALL
        SELECT e.id, cet.complex_type, e.parent_entity_id, eg.depth + 1
        FROM entities e, subentities_list eg, complex_entity_type cet
        WHERE e.id = eg.parent_entity_id
        and e.complex_entity_type_id=cet.id
	and cet.complex_type in ('ISSUE','SOLUTION')
        --and eg.depth < 2
       
)
select * from subentities_list 

select * from application_config

--old entity list
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
        and eg.depth < $depth
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
        and eg.depth < $depth
       #end
)
#end
select 
e.id
, e.title
#if($withContent)
, e.content
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
#if($userId)
,ev.vote user_vote
,ev.last_update user_vote_date
#end
#if($subtypesList)
, $subtypesList.size() subtypes_count
#foreach( $entityContext in $subtypesList.getValues())
#set($currentTable = "e${velocityCount}.id")
,count(distinct($currentTable)) $entityContext.getValue("simple_subtype")_subtype_count
#end	
#end
--if is a recent activity list
#if($parentId && !$entityType)
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
left outer join entities_votes v on (e2.id = v.entity_id)
group by e2.id	
) votes on (e.id = votes.entity_id)
#if($subtypesList)
#foreach( $entityContext in $subtypesList.getValues())
#set($currentTable = "e${velocityCount}")
left outer join entities $currentTable on (e.id = ${currentTable}.parent_entity_id and ${currentTable}.complex_entity_type_id=$entityContext.getValue("id"))
#end
#end
#if($userId)
left outer join entities_votes ev on(e.id = ev.entity_id and ev.user_id=$userId)
#end
inner join complex_entity_type cet on (e.complex_entity_type_id = cet.id)
#if($parentId)
#if($entityType)
join subentities_list idsList on (e.id = idsList.id and idsList.complex_type='$entityType')
#elseif($complexTypesIds)
join subentities_list idsList on (e.id = idsList.id and idsList.complex_type_id in $complexTypeIds)
#else
join subentities_list idsList on (e.id = idsList.id)
join activity_log actLog on (e.id = actLog.entity_id)
#end
#elseif($entityId)
join entity_upstream_hierarchy idsList on (e.id = idsList.id )
#end
#if($searchString)
where lower(e.title) like '%$searchString%' or lower(e.content) like '%$searchString%'
#end
group by 
e.id
, e.title
#if($withContent)
, e.content
#end
, e.insert_date
, e.last_update
, cet.complex_type
, e.creator_id
, idsList.depth
,votes.pro_votes,votes.opposed_votes,votes.total_votes
#if($userId)
,ev.vote,ev.last_update
#end
#if($parentId && !$entityType)
,e.parent_entity_id
,actLog.action_type_id
#end
order by $sortList
limit $itemsOnPage offset $itemsOnPage * ( $pageNumber - 1 )
--end

--entity info
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
else ((votes.pro_votes-votes.opposed_votes) * 1.0)/((votes.total_votes)*1.0) 
end popularity_index
#if($userId)
,ev.vote user_vote
,ev.last_update user_vote_date
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
left outer join entities_votes v on (e2.id = v.entity_id)
group by e2.id	
) votes on (e.id = votes.entity_id)
#if($userId)
left outer join entities_votes ev on(e.id = ev.entity_id and ev.user_id=$userId)
#end
inner join complex_entity_type cet on (e.complex_entity_type_id = cet.id)
where e.id=$entityId
group by e.id, e.title, e.content, e.insert_date, cet.complex_type,e.creator_id
,votes.pro_votes,votes.opposed_votes,votes.total_votes
#if($userId)
,ev.vote,ev.last_update
#end
--end

select * from entities where lower(title) like '%issue%' or lower(content) like '%issue%'

select distinct(complex_type) from complex_entity_type cet
where cet.id in ( select source_entity_type_id from entity_types_relations)
and cet.id in ( select target_entity_type_id from entity_types_relations)


--bla
WITH RECURSIVE subentities_list(id, complex_type_id,complex_type, parent_entity_id, depth) AS (
        SELECT e.id, cet.id complex_type_id,cet.complex_type, e.parent_entity_id, 0
        FROM entities e, complex_entity_type cet
        where e.parent_entity_id=25
        and e.complex_entity_type_id=cet.id
        
      UNION ALL
        SELECT e.id, cet.id complex_type_id,cet.complex_type, e.parent_entity_id, eg.depth + 1
        FROM entities e, subentities_list eg, complex_entity_type cet
        WHERE eg.id = e.parent_entity_id
        and e.complex_entity_type_id=cet.id
) 
--select * from subentities_list order by id

select * from action_types

select distinct(e.id),sl.complex_type from entities e
join subentities_list sl on (e.id = sl.id)
and sl.complex_type='ISSUE'
and (lower(e.title) like '%bla%' or lower(e.content) like '%bla%')


select tag_id,t.tag,count(*) from entities_tags et,tags t
where et.tag_id=t.id
group by tag_id,t.tag
order by count desc


select * from entities_tags where tag_id=61
