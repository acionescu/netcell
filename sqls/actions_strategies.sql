
insert into action_strategies 
values( (select id from complex_entity_type where complex_type='*')
, (select id from user_types  where type='GUEST')
, (select id from actions  where action='ro.problems.flows.login')
, (select id from entity_types where type='#')
, (select id from action_targets where target='HEADER')
,1
,(select id from complex_entity_type where complex_type='#')
,false
);

insert into action_strategies 
values( (select id from complex_entity_type where complex_type='*')
, (select id from user_types  where type='GUEST')
, (select id from actions  where action='ro.problems.flows.create-user')
, (select id from entity_types where type='#')
, (select id from action_targets where target='HEADER')
,2
,(select id from complex_entity_type where complex_type='#')
,false
);

insert into action_strategies 
values( (select id from complex_entity_type where complex_type='*')
, (select id from user_types  where type='MEMBER')
, (select id from actions  where action='ro.problems.flows.logout')
, (select id from entity_types where type='#')
, (select id from action_targets where target='HEADER')
,1
,(select id from complex_entity_type where complex_type='#')
,false
);
/*
insert into action_strategies 
values( (select id from complex_entity_type where complex_type='*')
, (select id from user_types  where type='MEMBER')
, (select id from actions  where name='user.update')
, (select id from entity_types where type='#')
, (select id from action_targets where target='TAB')
,2
,(select id from complex_entity_type where complex_type='#')
,false
);
*/


insert into action_strategies 
values( (select id from complex_entity_type where complex_type='METAGROUP')
, (select id from user_types  where type='MEMBER')
, (select id from actions  where name='entity.list.by.my.priority')
, (select id from entity_types where type='*')
, (select id from action_targets where target='TAB')
,10
,(select id from complex_entity_type where complex_type='*')
,false
);

insert into action_strategies 
values( (select id from complex_entity_type where complex_type='*')
, (select id from user_types  where type='GUEST')
, (select id from actions  where name='user.request.password.reset')
, (select id from entity_types where type='#')
, (select id from action_targets where target='LOGIN_FOOTER')
,1
,(select id from complex_entity_type where complex_type='#')
,false
);


insert into action_strategies 
values( (select id from complex_entity_type where complex_type='*')
, (select id from user_types  where type='GUEST')
, (select id from actions  where name='user.reset.password')
, (select id from entity_types where type='#')
, (select id from action_targets where target='URI')
,1
,(select id from complex_entity_type where complex_type='#')
,false
);


insert into action_strategies 
values( (select id from complex_entity_type where complex_type='METAGROUP')
, (select id from user_types  where type='*')
, (select id from actions  where name='entity.list.newest')
, (select id from entity_types where type='ISSUE')
, (select id from action_targets where target='TAB:ISSUE/LIST')
,1
,(select id from complex_entity_type where complex_type='ISSUE')
);

insert into action_strategies 
values( (select id from complex_entity_type where complex_type='METAGROUP')
, (select id from user_types  where type='*')
, (select id from actions  where name='entity.list.most_popular')
, (select id from entity_types where type='ISSUE')
, (select id from action_targets where target='TAB:ISSUE/LIST')
,2
,(select id from complex_entity_type where complex_type='ISSUE')
);


insert into action_strategies 
values( (select id from complex_entity_type where complex_type='METAGROUP')
, (select id from user_types  where type='*')
, (select id from actions  where name='entity.list.by.global.priority')
, (select id from entity_types where type='*')
, (select id from action_targets where target='TAB')
,4
,(select id from complex_entity_type where complex_type='*')
);
--
insert into action_strategies 
values( (select id from complex_entity_type where complex_type='ISSUE')
, (select id from user_types  where type='*')
, (select id from actions  where name='entity.list.newest')
, (select id from entity_types where type='COMMENT')
, (select id from action_targets where target='TAB:COMMENT/LIST')
,1
,(select id from complex_entity_type where complex_type='COMMENT')
);

insert into action_strategies 
values( (select id from complex_entity_type where complex_type='ISSUE')
, (select id from user_types  where type='*')
, (select id from actions  where name='entity.list.most_popular')
, (select id from entity_types where type='COMMENT')
, (select id from action_targets where target='TAB:COMMENT/LIST')
,2
,(select id from complex_entity_type where complex_type='COMMENT')
);

insert into action_strategies 
values( (select id from complex_entity_type where complex_type='ISSUE')
, (select id from user_types  where type='MEMBER')
, (select id from actions  where name='entity.create')
, (select id from entity_types where type='COMMENT')
, (select id from action_targets where target='TAB:COMMENT')
,3
,(select id from complex_entity_type where complex_type='COMMENT')
);

--
insert into action_strategies 
values( (select id from complex_entity_type where complex_type='ISSUE')
, (select id from user_types  where type='*')
, (select id from actions  where name='entity.list.newest')
, (select id from entity_types where type='SOLUTION')
, (select id from action_targets where target='TAB:SOLUTION/LIST')
,3
,(select id from complex_entity_type where complex_type='SOLUTION')
);

insert into action_strategies 
values( (select id from complex_entity_type where complex_type='ISSUE')
, (select id from user_types  where type='*')
, (select id from actions  where name='entity.list.most_popular')
, (select id from entity_types where type='SOLUTION')
, (select id from action_targets where target='TAB:SOLUTION/LIST')
,4
,(select id from complex_entity_type where complex_type='SOLUTION')
);

insert into action_strategies 
values( (select id from complex_entity_type where complex_type='ISSUE')
, (select id from user_types  where type='MEMBER')
, (select id from actions  where name='entity.create')
, (select id from entity_types where type='SOLUTION')
, (select id from action_targets where target='TAB:SOLUTION')
,5
,(select id from complex_entity_type where complex_type='SOLUTION')
);


insert into action_strategies 
values( (select id from complex_entity_type where complex_type='ISSUE')
, (select id from user_types  where type='MEMBER')
, (select id from actions  where name='entity.vote')
, (select id from entity_types where type='#')
, (select id from action_targets where target ='FOOTER')
,10
,(select id from complex_entity_type where complex_type='#')
);


insert into action_strategies 
values( (select id from complex_entity_type where complex_type='ISSUE')
, (select id from user_types  where type='MEMBER')
, (select id from actions  where name='entity.set.priority')
, (select id from entity_types where type='#')
, (select id from action_targets where target ='SUMMARY')
,11
,(select id from complex_entity_type where complex_type='#')
);

insert into action_strategies 
values( (select id from complex_entity_type where complex_type='ISSUE')
, (select id from user_types  where type='MEMBER')
, (select id from actions  where name='entity.set.status')
, (select id from entity_types where type='#')
, (select id from action_targets where target ='SUMMARY')
,12
,(select id from complex_entity_type where complex_type='#')
);

insert into action_strategies 
values( (select id from complex_entity_type where complex_type='METAGROUP')
, (select id from user_types  where type='MEMBER')
, (select id from actions  where name='entity.create.with_tags')
, (select id from entity_types where type='ISSUE')
, (select id from action_targets where target='TAB:ISSUE')
,3
,(select id from complex_entity_type where complex_type='ISSUE')
);

insert into action_strategies 
values( (select id from complex_entity_type where complex_type='SOLUTION')
, (select id from user_types  where type='*')
, (select id from actions  where name='entity.list.newest')
, (select id from entity_types where type='COMMENT')
, (select id from action_targets where target='TAB:COMMENT/LIST')
,1
,(select id from complex_entity_type where complex_type='COMMENT')
);

insert into action_strategies 
values( (select id from complex_entity_type where complex_type='SOLUTION')
, (select id from user_types  where type='*')
, (select id from actions  where name='entity.list.most_popular')
, (select id from entity_types where type='COMMENT')
, (select id from action_targets where target='TAB:COMMENT/LIST')
,2
,(select id from complex_entity_type where complex_type='COMMENT')
);

insert into action_strategies 
values( (select id from complex_entity_type where complex_type='SOLUTION')
, (select id from user_types  where type='MEMBER')
, (select id from actions  where name='entity.create')
, (select id from entity_types where type='COMMENT')
, (select id from action_targets where target='TAB:COMMENT')
,10
,(select id from complex_entity_type where complex_type='COMMENT')
);



insert into action_strategies 
values( (select id from complex_entity_type where complex_type='SOLUTION')
, (select id from user_types  where type='*')
, (select id from actions  where name='entity.list.newest')
, (select id from entity_types where type='ISSUE')
, (select id from action_targets where target='TAB:ISSUE/LIST')
,3
,(select id from complex_entity_type where complex_type='ISSUE')
);

insert into action_strategies 
values( (select id from complex_entity_type where complex_type='SOLUTION')
, (select id from user_types  where type='*')
, (select id from actions  where name='entity.list.most_popular')
, (select id from entity_types where type='ISSUE')
, (select id from action_targets where target='TAB:ISSUE/LIST')
,4
,(select id from complex_entity_type where complex_type='ISSUE')
);


insert into action_strategies 
values( (select id from complex_entity_type where complex_type='SOLUTION')
, (select id from user_types  where type='MEMBER')
, (select id from actions  where name='entity.create.with_tags')
, (select id from entity_types where type='ISSUE')
, (select id from action_targets where target='TAB:ISSUE')
,15
,(select id from complex_entity_type where complex_type='ISSUE')
);

insert into action_strategies 
values( (select id from complex_entity_type where complex_type='SOLUTION')
, (select id from user_types  where type='MEMBER')
, (select id from actions  where name='entity.vote')
, (select id from entity_types where type='#')
, (select id from action_targets where target='FOOTER')
,11
,(select id from complex_entity_type where complex_type='#')
);

insert into action_strategies 
values( (select id from complex_entity_type where complex_type='SOLUTION')
, (select id from user_types  where type='MEMBER')
, (select id from actions  where name='entity.set.priority')
, (select id from entity_types where type='#')
, (select id from action_targets where target ='SUMMARY')
,12
,(select id from complex_entity_type where complex_type='#')
);

insert into action_strategies 
values( (select id from complex_entity_type where complex_type='SOLUTION')
, (select id from user_types  where type='MEMBER')
, (select id from actions  where name='entity.set.status')
, (select id from entity_types where type='#')
, (select id from action_targets where target ='SUMMARY')
,13
,(select id from complex_entity_type where complex_type='#')
);



insert into action_strategies 
values( (select id from complex_entity_type where complex_type='COMMENT')
, (select id from user_types  where type='MEMBER')
, (select id from actions  where name='entity.vote')
, (select id from entity_types where type='#')
, (select id from action_targets where target='FOOTER')
,11
,(select id from complex_entity_type where complex_type='#')
);

insert into action_strategies 
values( (select id from complex_entity_type where complex_type='ISSUE')
, (select id from user_types  where type='CREATOR')
, (select id from actions  where name='entity.update')
, (select id from entity_types where type='ISSUE')
, (select id from action_targets where target='TAB')
,50
,(select id from complex_entity_type where complex_type='ISSUE')
,false
);

insert into action_strategies 
values( (select id from complex_entity_type where complex_type='SOLUTION')
, (select id from user_types  where type='CREATOR')
, (select id from actions  where name='entity.update')
, (select id from entity_types where type='SOLUTION')
, (select id from action_targets where target='TAB')
,50
,(select id from complex_entity_type where complex_type='SOLUTION')
,false
);

insert into action_strategies 
values( (select id from complex_entity_type where complex_type='ISSUE')
, (select id from user_types  where type='*')
, (select id from actions  where name='entity.upstream.hierarchy')
, (select id from entity_types where type='#')
, (select id from action_targets where target='HEADER')
,10
,(select id from complex_entity_type where complex_type='#')
,true
);

insert into action_strategies 
values( (select id from complex_entity_type where complex_type='SOLUTION')
, (select id from user_types  where type='*')
, (select id from actions  where name='entity.upstream.hierarchy')
, (select id from entity_types where type='#')
, (select id from action_targets where target='HEADER')
,10
,(select id from complex_entity_type where complex_type='#')
,true
);
--recent activity
insert into action_strategies 
values( (select id from complex_entity_type where complex_type='METAGROUP')
, (select id from user_types  where type='*')
, (select id from actions  where name='entity.list.recent.activity')
, (select id from entity_types where type='*')
, (select id from action_targets where target='TAB')
,0
,(select id from complex_entity_type where complex_type='*')
,true
);

insert into action_strategies 
values( (select id from complex_entity_type where complex_type='ISSUE')
, (select id from user_types  where type='*')
, (select id from actions  where name='entity.list.recent.activity')
, (select id from entity_types where type='*')
, (select id from action_targets where target='TAB')
,0
,(select id from complex_entity_type where complex_type='*')
,true
);

insert into action_strategies 
values( (select id from complex_entity_type where complex_type='SOLUTION')
, (select id from user_types  where type='*')
, (select id from actions  where name='entity.list.recent.activity')
, (select id from entity_types where type='*')
, (select id from action_targets where target='TAB')
,0
,(select id from complex_entity_type where complex_type='*')
,true
);

insert into action_strategies 
values( (select id from complex_entity_type where complex_type='*')
, (select id from user_types  where type='*')
, (select id from actions  where name='entity.get')
, (select id from entity_types where type='*')
, (select id from action_targets where target='#')
,0
,(select id from complex_entity_type where complex_type='*')
,true
);

insert into action_strategies 
values( (select id from complex_entity_type where complex_type='*')
, (select id from user_types  where type='*')
, (select id from actions  where name='statuses.get')
, (select id from entity_types where type='*')
, (select id from action_targets where target='#')
,0
,(select id from complex_entity_type where complex_type='*')
,true
);


select * from action_strategies
--delete from action_strategies


