
select * from entity_types

insert into entity_types(type,description) values('*','Any entity')
insert into entity_types(type,description) values('#','Self reference')
insert into entity_types(type,description) values('/','Root entity')
insert into entity_types(type,description) values('GROUP','Group entity')
insert into entity_types(type,description) values('ISSUE','Issue entity')
insert into entity_types(type,description) values('COMMENT','Comment entity')
insert into entity_types(type,description) values('SOLUTION','Solution entity')
insert into entity_types(type,description) values('METAGROUP','A public group entity')


select * from user_types

insert into user_types(type,description) values('CREATOR','A user that created an entity')
insert into user_types(type,description) values('SUBSCRIBER','A user that subscribed to an entity')
insert into user_types(type,description) values('*','Any user')
insert into user_types(type,description) values('COMMENTER','A user that added a comment')
insert into user_types(type,description) values('VOTER','A user that voted a certain entity')
insert into user_types(type,description) values('MEMBER','A user that has an account and logged in')
insert into user_types(type,description) values('GUEST','A user that is not logged in')


select * from action_types

insert into action_types(type,description) values('CREATE', 'Create an entity')
insert into action_types(type,description) values('READ', 'Read an entity')
insert into action_types(type,description) values('UPDATE', 'Update an entity')
insert into action_types(type,description) values('DELETE', 'Delete an entity')
insert into action_types(type,description) values('VOTE', 'Ability to vote a certain entity or action')
insert into action_types(type,description) values('PROPOSE_REFERENDUM', 'Propose referendum for a certain action')
insert into action_types(type,description) values('INVITE', 'Invite a user to subscribe to an entity')
insert into action_types(type,description) values('REQUEST_INVITATION', 'Request an invitation to a certain entity in order to become a subscriber')
insert into action_types(type,description) values('ACCEPT_INVITATION', 'Accept invitation to subscribe to an entity')
insert into action_types(type,description) values('FIND', 'Find an entity')
insert into action_types(type,description) values('LIST', 'List entities')
insert into action_types(type,description) values('SUBSCRIBE', 'Subscribe to a public entity')
insert into action_types(type,description) values('LOGIN', 'Login')
insert into action_types(type,description) values('LOGOUT', 'Logout')
insert into action_types(type,description) values('REGISTER', 'Create a user account')
insert into action_types(type,description) values('SET_PRIORITY', 'Set priority for an entity')
insert into action_types(type,description) values('SET_STATUS', 'Set status for an entity')

select * from actions


insert into actions(action,action_type_id,description,action_target_id)
values('ro.problems.flows.login'
,(select id from action_types where type='LOGIN')
,'Login'
,(select id from action_targets where target='USER')
)

insert into actions(action,action_type_id,description,action_target_id)
values('ro.problems.flows.register-user'
,(select id from action_types where type='REGISTER')
,'Register'
,(select id from action_targets where target='USER')
)

insert into actions(action,action_type_id,description,action_target_id)
values('ro.problems.flows.logout'
,(select id from action_types where type='LOGOUT')
,'Logout'
,(select id from action_targets where target='USER')
)

insert into actions(action,action_type_id,description,action_target_id)
values('ro.problems.flows.get-newest-issues'
,(select id from action_types where type='LIST')
,'Newest Issues'
,(select id from action_targets where target='ISSUE/LIST')
)


insert into actions(action,action_type_id,description,action_target_id)
values('ro.problems.flows.get-most-popular-issues'
,(select id from action_types where type='LIST')
,'Most Popular Issues'
,(select id from action_targets where target='ISSUE/LIST')
)

insert into actions(action,action_type_id,description,action_target_id)
values('ro.problems.flows.get-entity-info-by-id'
,(select id from action_types where type='READ')
,'Read an entity'
,(select id from action_targets where target='ENTITY')
)


insert into actions(action,action_type_id,description,action_target_id)
values('ro.problems.flows.get-entities-list'
,(select id from action_types where type='LIST')
,'List entities'
,(select id from action_targets where target='ENTITY')
)

insert into actions(action,action_type_id,description,action_target_id)
values('ro.problems.flows.create-entity'
,(select id from action_types where type='CREATE')
,'Create an issue'
,(select id from action_targets where target='ISSUE')
)

insert into actions(action,action_type_id,description,name)
values('ro.problems.flows.create-entity-with-tags'
,(select id from action_types where type='CREATE')
,'Create an entity with tags'
,'entity.create.with_tags'
)


insert into actions(action,action_type_id,description,name)
values('ro.problems.flows.vote-entity'
,(select id from action_types where type='VOTE')
,'Vote an entity'
,'entity.vote'
)

insert into actions(action,action_type_id,description,name)
values('ro.problems.flows.update-entity'
,(select id from action_types where type='UPDATE')
,'Update an entity'
,'entity.update'
)

insert into actions(action,action_type_id,description,name,params)
values('ro.problems.flows.get-entities-list'
,(select id from action_types where type='READ')
,'Open entity upstream hierarchy'
,'entity.upstream.hierarchy'
,'sortList=depth desc'
)


insert into actions(action,action_type_id,description,name,params)
values('ro.problems.flows.get-entities-list'
,(select id from action_types where type='READ')
,'Recent activity'
,'entity.list.recent.activity'
,'[{name=sortList,value=last_update desc}]'
)

insert into actions(action,action_type_id,description,name,params)
values('ro.problems.flows.save-entity-user-data'
,(select id from action_types where type='VOTE')
,'Vote entity'
,'entity.vote'
,'[{name=flag,value=vote}]'
)

insert into actions(action,action_type_id,description,name,params)
values('ro.problems.flows.save-entity-user-data'
,(select id from action_types where type='SET_PRIORITY')
,'Set priority for entity'
,'entity.set.priority'
,'[{name=flag,value=priority}]'
)

insert into actions(action,action_type_id,description,name,params)
values('ro.problems.flows.save-entity-user-data'
,(select id from action_types where type='SET_STATUS')
,'Set status for entity'
,'entity.set.status'
,'[{name=flag,value=status}]'
)


insert into actions(action,action_type_id,description,name,params)
values('ro.problems.flows.get-entities-list'
,(select id from action_types where type='LIST')
,'List by my priority'
,'entity.list.by.my.priority'
,'[{name=sortList,value=priority}]'
)

insert into actions(action,action_type_id,description,name,params)
values('ro.problems.flows.get-entities-list'
,(select id from action_types where type='LIST')
,'List by global priority'
,'entity.list.by.global.priority'
,'[{name=sortList,value=general_priority}]'
);

insert into actions(action,action_type_id,description,name,params)
values('ro.problems.flows.update-user'
,(select id from action_types where type='UPDATE')
,'Update user'
,'user.update'
,E'[{name=password,is-required=true,is-form-field=true,is-sensitive=true,field-input-regex="^[\w]{5,20}$"},{name=password-again,is-required=true,is-sensitive=true,is-form-field=true,field-input-regex="^[\w]{5,20}$"},{name=email,is-required=true,is-form-field=true,field-input-regex="(\w[-._\w]*\w@\w[-._\w]*\w\.\w{2,3})"}]'
);


insert into actions(action,action_type_id,description,name,params)
values('ro.problems.flows.send-password-reset-mail'
,(select id from action_types where type='UPDATE')
,'Request password reset'
,'user.request.password.reset'
,E'[{name=email,is-required=true,is-form-field=true,field-input-regex="(\w[-._\w]*\w@\w[-._\w]*\w\.\w{2,3})"}]'
);

insert into actions(action,action_type_id,description,name,params)
values('ro.problems.flows.update-user'
,(select id from action_types where type='UPDATE')
,'Reset user password'
,'user.reset.password'
,E'[{name=password,is-required=true,is-form-field=true,is-sensitive=true,field-input-regex="^[\w]{5,20}$"},{name=password-again,is-required=true,is-sensitive=true,is-form-field=true,field-input-regex="^[\w]{5,20}$"}]'
);


insert into actions(action,action_type_id,description,name)
values('ro.problems.flows.get-entity-info-by-id'
,(select id from action_types where type='READ')
,'Get entity info'
,'entity.get'
);

insert into actions(action,action_type_id,description,name)
values('ro.problems.flows.get-statuses'
,(select id from action_types where type='READ')
,'Get statuses'
,'statuses.get'
);


select * from actions

select * from users where email='individul.x@gmail.com'

update actions 
set action_target_id=(select id from action_targets where target='ISSUE')
, description='Create an issue'
where action='ro.problems.flows.create-entity'

select * from complex_entity_type  



insert into complex_entity_type(complex_type) values('/')
insert into complex_entity_type(complex_type) values('/GROUP')
insert into complex_entity_type(complex_type) values('/METAGROUP')
insert into complex_entity_type(complex_type) values('/METAGROUP/ISSUE')
insert into complex_entity_type(complex_type,allow_tag,allow_title_duplicates) values('/GROUP/ISSUE','y','n')
insert into complex_entity_type(complex_type) values('/GROUP/ISSUE/COMMENT')
insert into complex_entity_type(complex_type) values('/METAGROUP/ISSUE/COMMENT')
insert into complex_entity_type(complex_type) values('/*/COMMENT')
insert into complex_entity_type(complex_type) values('/*/ISSUE')
insert into complex_entity_type(complex_type) values('*')
insert into complex_entity_type(complex_type,allow_title_duplicates) values('/METAGROUP/ISSUE/SOLUTION','n')
insert into complex_entity_type(complex_type) values('/METAGROUP/ISSUE/SOLUTION/COMMENT')
insert into complex_entity_type(complex_type) values('#')


select * from entity_types_relations

insert into entity_types_relations 
values(
(select id from complex_entity_type where complex_type='METAGROUP')
,(select id from complex_entity_type where complex_type='ISSUE')
);

insert into entity_types_relations 
values(
(select id from complex_entity_type where complex_type='ISSUE')
,(select id from complex_entity_type where complex_type='COMMENT')
);

insert into entity_types_relations 
values(
(select id from complex_entity_type where complex_type='ISSUE')
,(select id from complex_entity_type where complex_type='SOLUTION')
);

insert into entity_types_relations 
values(
(select id from complex_entity_type where complex_type='SOLUTION')
,(select id from complex_entity_type where complex_type='COMMENT')
);

insert into entity_types_relations 
values(
(select id from complex_entity_type where complex_type='SOLUTION')
,(select id from complex_entity_type where complex_type='ISSUE')
);

select * from action_targets

insert into action_targets(target) values('USER')
insert into action_targets(target) values('ISSUE/LIST')
insert into action_targets(target) values('ISSUE/CREATE')
insert into action_targets(target) values('ISSUE')
insert into action_targets(target) values('COMMENT')
insert into action_targets(target) values('SOLUTION')
insert into action_targets(target) values('TAB')
insert into action_targets(target) values('#')
insert into action_targets(target) values('TAB:RECENT_ACTIVITY')
insert into action_targets(target) values('SUMMARY')
insert into action_targets(target) values('SIMPLE_HEADER')
insert into action_targets(target) values('TAB')
insert into action_targets(target) values('LOGIN_FOOTER')
insert into action_targets(target) values('URI')

select * from action_strategies

select 
cet.complex_type
,ut.type user_type
,act.action
,act.id action_id
, et.type target_entity_type 
,cet2.complex_type target_entity_complex_type
, acts.action_target_id
, atg.target
, acts.order_index
from action_strategies acts
join complex_entity_type cet on (acts.complex_entity_type_id = cet.id)
join complex_entity_type cet2 on (acts.target_entity_complex_type_id = cet2.id)
join user_types ut on (acts.user_type_id = ut.id)
join actions act on (acts.action_id=act.id)
join entity_types et on (acts.entity_type_id=et.id)
join action_targets atg on (acts.action_target_id=atg.id)
order by cet.complex_type,acts.order_index


--create first user GOD
select * from users

insert into users(username,password) values('metauser',md5('viitorulesteacum'));

--create ROOT entity

alter table entities
drop constraint entities_self_ref_fk
,alter column parent_entity_id drop not null;

insert into entities(title,content,creator_id,entity_type_id,complex_entity_type_id)
values('/','This is the primordial entity, the one under which everything else will be created. 
It''s like the BIG BANG, that makes possible for everything else to come into existence.'
,(select id from users where username='metauser')
,(select id from entity_types  where type='/')
,(select id from complex_entity_type  where complex_type='/')
);

update entities
set parent_entity_id = (select id from entities where title='/')
where title='/';

alter table entities
add constraint entities_self_ref_fk
foreign key (parent_entity_id) references entities(id)
, alter column parent_entity_id set not null;

insert into entities(title,content,creator_id,entity_type_id,complex_entity_type_id,parent_entity_id)
values('Grup rădăcină','Acesta este grupul rădăcină ce va găzdui toate dezbaterile ulterioare.'
,(select id from users where username='metauser')
,(select id from entity_types  where type='METAGROUP')
,(select id from complex_entity_type  where complex_type='METAGROUP')
,(select id from entities where title='/')
);

delete from activity_log;


update application_config
set int_value=(select id from entities where title='Grup rădăcină')
where param_name='default.entity.id'
