--
-- PostgreSQL database dump
--

-- Started on 2010-08-03 12:30:41 EEST

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

SET search_path = public, pg_catalog;

--
-- TOC entry 2008 (class 0 OID 0)
-- Dependencies: 1581
-- Name: action_targets_seq; Type: SEQUENCE SET; Schema: public; Owner: metaguvernare
--

SELECT pg_catalog.setval('action_targets_seq', 180, true);


--
-- TOC entry 2009 (class 0 OID 0)
-- Dependencies: 1565
-- Name: action_types_seq; Type: SEQUENCE SET; Schema: public; Owner: metaguvernare
--

SELECT pg_catalog.setval('action_types_seq', 17, true);


--
-- TOC entry 2010 (class 0 OID 0)
-- Dependencies: 1578
-- Name: actions_seq; Type: SEQUENCE SET; Schema: public; Owner: metaguvernare
--

SELECT pg_catalog.setval('actions_seq', 220, true);


--
-- TOC entry 2011 (class 0 OID 0)
-- Dependencies: 1585
-- Name: activity_log_seq; Type: SEQUENCE SET; Schema: public; Owner: metaguvernare
--

SELECT pg_catalog.setval('activity_log_seq', 48, true);


--
-- TOC entry 2012 (class 0 OID 0)
-- Dependencies: 1571
-- Name: complex_entity_types_seq; Type: SEQUENCE SET; Schema: public; Owner: metaguvernare
--

SELECT pg_catalog.setval('complex_entity_types_seq', 13, true);


--
-- TOC entry 2013 (class 0 OID 0)
-- Dependencies: 1564
-- Name: entities_seq; Type: SEQUENCE SET; Schema: public; Owner: metaguvernare
--

SELECT pg_catalog.setval('entities_seq', 620, true);


--
-- TOC entry 2014 (class 0 OID 0)
-- Dependencies: 1577
-- Name: entities_tags_seq; Type: SEQUENCE SET; Schema: public; Owner: metaguvernare
--

SELECT pg_catalog.setval('entities_tags_seq', 280, true);


--
-- TOC entry 2015 (class 0 OID 0)
-- Dependencies: 1566
-- Name: entity_types_seq; Type: SEQUENCE SET; Schema: public; Owner: metaguvernare
--

SELECT pg_catalog.setval('entity_types_seq', 9, true);


--
-- TOC entry 2016 (class 0 OID 0)
-- Dependencies: 1588
-- Name: statuses_seq; Type: SEQUENCE SET; Schema: public; Owner: metaguvernare
--

SELECT pg_catalog.setval('statuses_seq', 16, true);


--
-- TOC entry 2017 (class 0 OID 0)
-- Dependencies: 1575
-- Name: tags_seq; Type: SEQUENCE SET; Schema: public; Owner: metaguvernare
--

SELECT pg_catalog.setval('tags_seq', 240, true);


--
-- TOC entry 2018 (class 0 OID 0)
-- Dependencies: 1567
-- Name: user_types_seq; Type: SEQUENCE SET; Schema: public; Owner: metaguvernare
--

SELECT pg_catalog.setval('user_types_seq', 7, true);


--
-- TOC entry 2019 (class 0 OID 0)
-- Dependencies: 1568
-- Name: users_seq; Type: SEQUENCE SET; Schema: public; Owner: metaguvernare
--

SELECT pg_catalog.setval('users_seq', 13, true);


--
-- TOC entry 2000 (class 0 OID 49580)
-- Dependencies: 1580
-- Data for Name: action_targets; Type: TABLE DATA; Schema: public; Owner: metaguvernare
--

COPY action_targets (id, target) FROM stdin;
1	HEADER
21	TAB:ISSUE/LIST
22	TAB:ISSUE
41	TAB:ISSUE/CREATE
61	TAB:COMMENT/LIST
62	TAB:SOLUTION/LIST
63	TAB:COMMENT
64	TAB:SOLUTION
65	FOOTER
81	TAB
82	#
101	SUMMARY
121	SIMPLE_HEADER
141	TAB:USER
161	LOGIN_FOOTER
162	URI
\.


--
-- TOC entry 1988 (class 0 OID 33088)
-- Dependencies: 1559
-- Data for Name: action_types; Type: TABLE DATA; Schema: public; Owner: metaguvernare
--

COPY action_types (id, type, description) FROM stdin;
1	CREATE	Create an entity
2	READ	Read an entity
3	UPDATE	Update an entity
4	DELETE	Delete an entity
5	VOTE	Vote for an entity
6	PROPOSE_REFERENDUM	Propose referendum for a certain action
7	INVITE	Invite a user to subscribe to an entity
8	REQUEST_INVITATION	Request an invitation to a certain entity in order to become a subscriber
9	FIND	Find an entity
10	LIST	List entities
11	ACCEPT_INVITATION	Accept invitation to subscribe to an entity
12	SUBSCRIBE	Subscribe to a public entity
13	LOGIN	Login
14	LOGOUT	Logout
15	REGISTER	Create a user account
16	SET_PRIORITY	Set priority for an entity
17	SET_STATUS	Set status for an entity
\.


--
-- TOC entry 1999 (class 0 OID 49556)
-- Dependencies: 1579 1988
-- Data for Name: actions; Type: TABLE DATA; Schema: public; Owner: metaguvernare
--

COPY actions (id, action, action_type_id, description, name, params) FROM stdin;
81	ro.problems.flows.create-entity-with-tags	1	Create an entity with tags	entity.create.with_tags	[{name=title,is-required=true,is-form-field=true,field-width=60%,field-input-regex="^[\\w\\W]{2,120}$"},{name=content,is-required=true,is-form-field=true,field-input-regex="^[\\w\\W]{0,10000}$",field-ui-type=richtextarea,field-width=100%,field-height=100%},{name=tags,is-required=true,is-form-field=true,field-width=30%}]
122	ro.problems.flows.get-entities-list	2	Open entity upstream hierarchy	entity.upstream.hierarchy	[{name=sortList,value=depth desc}]
124	ro.problems.flows.save-entity-user-data	5	Vote entity	entity.vote	[{name=flag,value=vote}]
1	ro.problems.flows.login	13	Login	user.login	[{name=username,is-required=true,is-form-field=true},{name=password,is-required=true,is-form-field=true,is-sensitive=true}]
125	ro.problems.flows.save-entity-user-data	16	Set priority for entity	entity.set.priority	[{name=flag,value=priority}]
126	ro.problems.flows.save-entity-user-data	17	Set status for entity	entity.set.status	[{name=flag,value=status}]
2	ro.problems.flows.create-user	15	Register	user.register	[{name=username,is-required=true,is-form-field=true,field-input-regex="^[\\w]{5,20}$"},{name=password,is-required=true,is-form-field=true,is-sensitive=true,field-input-regex="^[\\w]{5,20}$"},{name=password-again,is-required=true,is-sensitive=true,is-form-field=true,field-input-regex="^[\\w]{5,20}$"},{name=email,is-required=true,is-form-field=true,field-input-regex="(\\w[-._\\w]*\\w@\\w[-._\\w]*\\w\\.\\w{2,3})"}]
123	ro.problems.flows.get-entities-list	2	Recent activity	entity.list.recent.activity	[{name=sortList,value=actLog.action_timestamp desc}]
3	ro.problems.flows.logout	14	Logout	user.logout	\N
22	ro.problems.flows.get-entities-list	10	List entities	entity.list	\N
21	ro.problems.flows.get-entity-info-by-id	2	Read an entity	entity.read	\N
141	ro.problems.flows.get-entities-list	10	List by my priority	entity.list.by.my.priority	[{name=sortList,value=priority},{name=userActionColumn,value=priority},{name=filterByUser,value=true}]
161	ro.problems.flows.get-entities-list	10	List by global priority	entity.list.by.global.priority	[{name=sortList,value=general_priority},{name=userActionColumn,value=priority}]
41	ro.problems.flows.create-entity	1	Create an entity	entity.create	[{name=title,is-required=true,is-form-field=true,field-width=60%,field-input-regex="^[\\w\\W]{2,120}$"},{name=content,is-required=true,is-form-field=true,field-input-regex="^[\\w\\W]{0,10000}$",field-ui-type=richtextarea,field-width=100%,field-height=100%}]
121	ro.problems.flows.update-entity	3	Update an entity	entity.update	[{name=title,is-required=true,is-form-field=true,field-width=60%,field-input-regex="^[\\w\\W]{2,120}$"},{name=content,is-required=true,is-form-field=true,field-input-regex="^[\\w\\W]{0,10000}$",field-ui-type=richtextarea,field-width=100%,field-height=100%}]
4	ro.problems.flows.get-entities-list	10	Newest Issues	entity.list.newest	[{name=sortList,value=insert_date desc}]
5	ro.problems.flows.get-entities-list	10	Most Popular Issues	entity.list.most_popular	[{name=sortList,value="popularity_index desc, total_votes desc,id"}]
194	ro.problems.flows.update-user	3	Update user	user.update	[{name=password,is-required=true,is-form-field=true,is-sensitive=true,field-input-regex="^[\\w]{5,20}$"},{name=password-again,is-required=true,is-sensitive=true,is-form-field=true,field-input-regex="^[\\w]{5,20}$"},{name=email,is-required=true,is-form-field=true,field-input-regex="(\\w[-._\\w]*w@w[-._\\w]*\\w\\.\\w{2,3})"}]
201	ro.problems.flows.send-password-reset-mail	3	Request password reset	user.request.password.reset	[{name=email,is-required=true,is-form-field=true,field-input-regex="(\\w[-._\\w]*\\w@\\w[-._\\w]*\\w\\.\\w{2,3})"}]
202	ro.problems.flows.update-user	3	Reset user password	user.reset.password	[{name=password,is-required=true,is-form-field=true,is-sensitive=true,field-input-regex="^[\\w]{5,20}$"},{name=password-again,is-required=true,is-sensitive=true,is-form-field=true,field-input-regex="^[\\w]{5,20}$"}]
\.


--
-- TOC entry 1994 (class 0 OID 41274)
-- Dependencies: 1570
-- Data for Name: complex_entity_type; Type: TABLE DATA; Schema: public; Owner: metaguvernare
--

COPY complex_entity_type (id, complex_type, allow_tag, allow_title_duplicates, show_post_info, list_with_content, allow_voting, allow_recursive_list, is_subtype, allow_status, allow_refresh) FROM stdin;
6	COMMENT	n	y	y	y	y	n	y	n	n
1	/	n	y	y	n	y	n	n	n	n
13	#	n	y	y	n	y	n	n	n	n
8	*	y	y	y	n	y	y	n	y	n
2	METAGROUP	n	n	n	n	n	n	n	n	y
3	ISSUE	y	n	y	n	y	y	y	y	y
11	SOLUTION	n	n	y	y	y	n	y	y	y
\.


--
-- TOC entry 1989 (class 0 OID 33093)
-- Dependencies: 1560
-- Data for Name: entity_types; Type: TABLE DATA; Schema: public; Owner: metaguvernare
--

COPY entity_types (id, type, description) FROM stdin;
2	GROUP	Group entity
3	ISSUE	Issue entity
4	COMMENT	Comment entity
5	SOLUTION	Solution entity
1	/	Root entity
7	METAGROUP	A public group entity
6	*	Any entity
9	#	Self reference
\.


--
-- TOC entry 1987 (class 0 OID 33083)
-- Dependencies: 1558
-- Data for Name: user_types; Type: TABLE DATA; Schema: public; Owner: metaguvernare
--

COPY user_types (id, type, description) FROM stdin;
1	CREATOR	A user that created an entity
2	SUBSCRIBER	A user that subscribed to an entity
3	*	Any user
4	COMMENTER	A user that added a comment
5	VOTER	A user that voted a certain entity
6	MEMBER	A user that has an account and logged in
7	GUEST	A user that is not logged in
\.


--
-- TOC entry 1995 (class 0 OID 41288)
-- Dependencies: 1572 1987 1989 1994 1999 2000
-- Data for Name: action_strategies; Type: TABLE DATA; Schema: public; Owner: metaguvernare
--

COPY action_strategies (complex_entity_type_id, user_type_id, action_id, entity_type_id, action_target_id, order_index, target_entity_complex_type_id, allow_read_to_all) FROM stdin;
8	7	1	9	1	1	13	f
8	7	2	9	1	2	13	f
8	6	3	9	1	1	13	f
2	6	141	6	81	10	8	f
8	7	201	9	161	1	13	f
8	7	202	9	162	1	13	f
2	3	4	3	21	1	3	t
2	3	5	3	21	2	3	t
2	3	161	6	81	4	8	t
3	3	4	4	61	1	6	t
3	3	5	4	61	2	6	t
3	6	41	4	63	3	6	t
3	3	4	5	62	3	11	t
3	3	5	5	62	4	11	t
3	6	41	5	64	5	11	t
3	6	124	9	65	10	13	t
3	6	125	9	101	11	13	t
3	6	126	9	101	12	13	t
2	6	81	3	22	3	3	t
11	3	4	4	61	1	6	t
11	3	5	4	61	2	6	t
11	6	41	4	63	10	6	t
11	3	4	3	21	3	3	t
11	3	5	3	21	4	3	t
11	6	81	3	22	15	3	t
11	6	124	9	65	11	13	t
11	6	125	9	101	12	13	t
11	6	126	9	101	13	13	t
6	6	124	9	65	11	13	t
3	1	121	3	81	50	3	f
11	1	121	5	81	50	11	f
3	3	122	9	1	10	13	t
11	3	122	9	1	10	13	t
2	3	123	6	81	0	8	t
3	3	123	6	81	0	8	t
11	3	123	6	81	0	8	t
\.


--
-- TOC entry 2001 (class 0 OID 49593)
-- Dependencies: 1582
-- Data for Name: application_config; Type: TABLE DATA; Schema: public; Owner: metaguvernare
--

COPY application_config (param_name, string_value, int_value, number_value) FROM stdin;
default.entity.id	\N	25	\N
logable.actions	CREATE,UPDATE	\N	\N
max.priority	\N	10	\N
app.entity.id	\N	247	\N
\.




--
-- TOC entry 1992 (class 0 OID 33168)
-- Dependencies: 1563 1990 1986
-- Data for Name: entities_subscribers; Type: TABLE DATA; Schema: public; Owner: metaguvernare
--

COPY entities_subscribers (entity_id, user_id) FROM stdin;
\.


--
-- TOC entry 2002 (class 0 OID 49680)
-- Dependencies: 1583 1994 1994
-- Data for Name: entity_types_relations; Type: TABLE DATA; Schema: public; Owner: metaguvernare
--

COPY entity_types_relations (source_entity_type_id, target_entity_type_id) FROM stdin;
2	3
3	6
3	11
11	6
11	3
\.


-- Completed on 2010-08-03 12:30:41 EEST

--
-- PostgreSQL database dump complete
--

