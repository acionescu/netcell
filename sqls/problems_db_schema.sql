--
-- PostgreSQL database dump
--

-- Started on 2010-08-03 10:55:26 EEST

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- TOC entry 370 (class 2612 OID 49518)
-- Name: plpgsql; Type: PROCEDURAL LANGUAGE; Schema: -; Owner: metaguvernare
--

CREATE PROCEDURAL LANGUAGE plpgsql;


ALTER PROCEDURAL LANGUAGE plpgsql OWNER TO metaguvernare;

SET search_path = public, pg_catalog;

--
-- TOC entry 21 (class 1255 OID 49875)
-- Dependencies: 370 6
-- Name: compute_user_hash(bigint); Type: FUNCTION; Schema: public; Owner: metaguvernare
--

CREATE FUNCTION compute_user_hash(user_id bigint) RETURNS text
    LANGUAGE plpgsql
    AS $$

BEGIN
return (select md5(username||password||email||localtimestamp) from users where id=user_id);
END;
$$;


ALTER FUNCTION public.compute_user_hash(user_id bigint) OWNER TO metaguvernare;

--
-- TOC entry 19 (class 1255 OID 49521)
-- Dependencies: 6 370
-- Name: entities_last_update(); Type: FUNCTION; Schema: public; Owner: metaguvernare
--

CREATE FUNCTION entities_last_update() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
NEW.last_update := localtimestamp;
RETURN NEW;
END;$$;


ALTER FUNCTION public.entities_last_update() OWNER TO metaguvernare;

--
-- TOC entry 22 (class 1255 OID 49862)
-- Dependencies: 6 370
-- Name: get_general_priority(bigint); Type: FUNCTION; Schema: public; Owner: metaguvernare
--

CREATE FUNCTION get_general_priority(entity_id bigint) RETURNS bigint
    LANGUAGE plpgsql
    AS $$
DECLARE 
	pr RECORD;
	output bigint :=0;
	p_sum numeric :=0;
	p_avg numeric :=0;
	users_count integer:=0;
	total_users integer:=0;
BEGIN

FOR pr IN SELECT * FROM entities_priorities_count epc where epc.entity_id=entity_id LOOP 
	p_sum:=(p_sum + pr.priority*pr.count);
	users_count:=(users_count+pr.count);
END LOOP;
if(users_count > 0) then
p_avg:=(p_sum/users_count);
select into total_users count(*) from users;
output:=floor(11 - (11 - p_avg)*users_count/(total_users-1));
end if;	
RETURN output;
END;
$$;


ALTER FUNCTION public.get_general_priority(entity_id bigint) OWNER TO metaguvernare;

--
-- TOC entry 20 (class 1255 OID 49740)
-- Dependencies: 6 370
-- Name: log_activity(); Type: FUNCTION; Schema: public; Owner: metaguvernare
--

CREATE FUNCTION log_activity() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
IF(TG_OP='INSERT') THEN
INSERT INTO ACTIVITY_LOG(ENTITY_ID,ACTION_TYPE_ID)
VALUES(NEW.ID,(SELECT ID FROM ACTION_TYPES WHERE TYPE='CREATE'));
ELSIF(TG_OP='UPDATE') THEN
INSERT INTO ACTIVITY_LOG(ENTITY_ID,ACTION_TYPE_ID)
VALUES(NEW.ID,(SELECT ID FROM ACTION_TYPES WHERE TYPE='UPDATE'));
END IF;
RETURN NEW;
END;$$;


ALTER FUNCTION public.log_activity() OWNER TO metaguvernare;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 1572 (class 1259 OID 41288)
-- Dependencies: 1894 1895 6
-- Name: action_strategies; Type: TABLE; Schema: public; Owner: metaguvernare; Tablespace: 
--

CREATE TABLE action_strategies (
    complex_entity_type_id bigint NOT NULL,
    user_type_id bigint NOT NULL,
    action_id bigint NOT NULL,
    entity_type_id bigint NOT NULL,
    action_target_id bigint NOT NULL,
    order_index bigint DEFAULT 0 NOT NULL,
    target_entity_complex_type_id bigint,
    allow_read_to_all boolean DEFAULT true NOT NULL
);


ALTER TABLE public.action_strategies OWNER TO metaguvernare;

--
-- TOC entry 1581 (class 1259 OID 49585)
-- Dependencies: 6
-- Name: action_targets_seq; Type: SEQUENCE; Schema: public; Owner: metaguvernare
--

CREATE SEQUENCE action_targets_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 20;


ALTER TABLE public.action_targets_seq OWNER TO metaguvernare;

--
-- TOC entry 1580 (class 1259 OID 49580)
-- Dependencies: 1900 6
-- Name: action_targets; Type: TABLE; Schema: public; Owner: metaguvernare; Tablespace: 
--

CREATE TABLE action_targets (
    id bigint DEFAULT nextval('action_targets_seq'::regclass) NOT NULL,
    target character varying(50)
);


ALTER TABLE public.action_targets OWNER TO metaguvernare;

--
-- TOC entry 1565 (class 1259 OID 33187)
-- Dependencies: 6
-- Name: action_types_seq; Type: SEQUENCE; Schema: public; Owner: metaguvernare
--

CREATE SEQUENCE action_types_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.action_types_seq OWNER TO metaguvernare;

--
-- TOC entry 1559 (class 1259 OID 33088)
-- Dependencies: 1878 6
-- Name: action_types; Type: TABLE; Schema: public; Owner: metaguvernare; Tablespace: 
--

CREATE TABLE action_types (
    id bigint DEFAULT nextval('action_types_seq'::regclass) NOT NULL,
    type character varying(20) NOT NULL,
    description character varying(100)
);


ALTER TABLE public.action_types OWNER TO metaguvernare;

--
-- TOC entry 1578 (class 1259 OID 49554)
-- Dependencies: 6
-- Name: actions_seq; Type: SEQUENCE; Schema: public; Owner: metaguvernare
--

CREATE SEQUENCE actions_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 20;


ALTER TABLE public.actions_seq OWNER TO metaguvernare;

--
-- TOC entry 1579 (class 1259 OID 49556)
-- Dependencies: 1899 6
-- Name: actions; Type: TABLE; Schema: public; Owner: metaguvernare; Tablespace: 
--

CREATE TABLE actions (
    id bigint DEFAULT nextval('actions_seq'::regclass) NOT NULL,
    action character varying(100) NOT NULL,
    action_type_id bigint NOT NULL,
    description character varying(500),
    name character varying(50),
    params character varying(1000)
);


ALTER TABLE public.actions OWNER TO metaguvernare;

--
-- TOC entry 1585 (class 1259 OID 49722)
-- Dependencies: 6
-- Name: activity_log_seq; Type: SEQUENCE; Schema: public; Owner: metaguvernare
--

CREATE SEQUENCE activity_log_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.activity_log_seq OWNER TO metaguvernare;

--
-- TOC entry 1584 (class 1259 OID 49718)
-- Dependencies: 1901 1902 6
-- Name: activity_log; Type: TABLE; Schema: public; Owner: metaguvernare; Tablespace: 
--

CREATE TABLE activity_log (
    id bigint DEFAULT nextval('activity_log_seq'::regclass) NOT NULL,
    entity_id bigint,
    action_type_id bigint,
    action_timestamp timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone
);


ALTER TABLE public.activity_log OWNER TO metaguvernare;

--
-- TOC entry 1582 (class 1259 OID 49593)
-- Dependencies: 6
-- Name: application_config; Type: TABLE; Schema: public; Owner: metaguvernare; Tablespace: 
--

CREATE TABLE application_config (
    param_name character varying(100) NOT NULL,
    string_value character varying(1000),
    int_value bigint,
    number_value double precision
);


ALTER TABLE public.application_config OWNER TO metaguvernare;

--
-- TOC entry 1571 (class 1259 OID 41277)
-- Dependencies: 6
-- Name: complex_entity_types_seq; Type: SEQUENCE; Schema: public; Owner: metaguvernare
--

CREATE SEQUENCE complex_entity_types_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.complex_entity_types_seq OWNER TO metaguvernare;

--
-- TOC entry 1570 (class 1259 OID 41274)
-- Dependencies: 1884 1885 1886 1887 1888 1889 1890 1891 1892 1893 6
-- Name: complex_entity_type; Type: TABLE; Schema: public; Owner: metaguvernare; Tablespace: 
--

CREATE TABLE complex_entity_type (
    id bigint DEFAULT nextval('complex_entity_types_seq'::regclass) NOT NULL,
    complex_type character varying(500) NOT NULL,
    allow_tag character(1) DEFAULT 'n'::bpchar,
    allow_title_duplicates character(1) DEFAULT 'y'::bpchar,
    show_post_info character(1) DEFAULT 'y'::bpchar,
    list_with_content character(1) DEFAULT 'n'::bpchar,
    allow_voting character(1) DEFAULT 'y'::bpchar,
    allow_recursive_list character(1) DEFAULT 'n'::bpchar NOT NULL,
    is_subtype character(1) DEFAULT 'y'::bpchar NOT NULL,
    allow_status character(1) DEFAULT 'n'::bpchar NOT NULL,
    allow_refresh character(1) DEFAULT 'n'::bpchar NOT NULL
);


ALTER TABLE public.complex_entity_type OWNER TO metaguvernare;

--
-- TOC entry 1564 (class 1259 OID 33183)
-- Dependencies: 6
-- Name: entities_seq; Type: SEQUENCE; Schema: public; Owner: metaguvernare
--

CREATE SEQUENCE entities_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 20;


ALTER TABLE public.entities_seq OWNER TO metaguvernare;

--
-- TOC entry 1561 (class 1259 OID 33123)
-- Dependencies: 1880 1881 1882 6
-- Name: entities; Type: TABLE; Schema: public; Owner: metaguvernare; Tablespace: 
--

CREATE TABLE entities (
    id bigint DEFAULT nextval('entities_seq'::regclass) NOT NULL,
    title character varying(120) NOT NULL,
    content character varying(10000) NOT NULL,
    creator_id bigint NOT NULL,
    entity_type_id bigint NOT NULL,
    insert_date timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    last_update timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    complex_entity_type_id bigint NOT NULL,
    parent_entity_id bigint NOT NULL
);


ALTER TABLE public.entities OWNER TO metaguvernare;

--
-- TOC entry 1586 (class 1259 OID 49767)
-- Dependencies: 1903 1904 1905 6
-- Name: entities_users; Type: TABLE; Schema: public; Owner: metaguvernare; Tablespace: 
--

CREATE TABLE entities_users (
    entity_id bigint NOT NULL,
    user_id bigint NOT NULL,
    vote character(1),
    priority bigint,
    status_id bigint,
    last_vote_update timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    last_priority_update timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    last_status_update timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone
);


ALTER TABLE public.entities_users OWNER TO metaguvernare;

--
-- TOC entry 1592 (class 1259 OID 49863)
-- Dependencies: 1682 6
-- Name: entities_priorities; Type: VIEW; Schema: public; Owner: metaguvernare
--

CREATE VIEW entities_priorities AS
    SELECT DISTINCT entities_users.entity_id, get_general_priority(entities_users.entity_id) AS priority FROM entities_users WHERE (entities_users.priority IS NOT NULL);


ALTER TABLE public.entities_priorities OWNER TO metaguvernare;

--
-- TOC entry 1589 (class 1259 OID 49842)
-- Dependencies: 1679 6
-- Name: entities_priorities_count; Type: VIEW; Schema: public; Owner: metaguvernare
--

CREATE VIEW entities_priorities_count AS
    SELECT eu.entity_id, eu.priority, count(*) AS count FROM entities_users eu WHERE (eu.priority IS NOT NULL) GROUP BY eu.entity_id, eu.priority;


ALTER TABLE public.entities_priorities_count OWNER TO metaguvernare;

--
-- TOC entry 1590 (class 1259 OID 49850)
-- Dependencies: 1680 6
-- Name: entities_statuses_count; Type: VIEW; Schema: public; Owner: metaguvernare
--

CREATE VIEW entities_statuses_count AS
    SELECT eu.entity_id, eu.status_id, count(*) AS count FROM entities_users eu WHERE (eu.status_id IS NOT NULL) GROUP BY eu.entity_id, eu.status_id ORDER BY count(*) DESC;


ALTER TABLE public.entities_statuses_count OWNER TO metaguvernare;

--
-- TOC entry 1591 (class 1259 OID 49854)
-- Dependencies: 1681 6
-- Name: entities_statuses; Type: VIEW; Schema: public; Owner: metaguvernare
--

CREATE VIEW entities_statuses AS
    SELECT esc.entity_id, esc.status_id, esc.count FROM entities_statuses_count esc WHERE ((esc.count = (SELECT max(entities_statuses_count.count) AS max FROM entities_statuses_count WHERE (entities_statuses_count.entity_id = esc.entity_id))) AND (esc.status_id = (SELECT min(entities_statuses_count.status_id) AS min FROM entities_statuses_count WHERE ((entities_statuses_count.entity_id = esc.entity_id) AND (entities_statuses_count.count = esc.count)))));


ALTER TABLE public.entities_statuses OWNER TO metaguvernare;

--
-- TOC entry 1563 (class 1259 OID 33168)
-- Dependencies: 6
-- Name: entities_subscribers; Type: TABLE; Schema: public; Owner: metaguvernare; Tablespace: 
--

CREATE TABLE entities_subscribers (
    entity_id bigint NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE public.entities_subscribers OWNER TO metaguvernare;

--
-- TOC entry 1577 (class 1259 OID 49551)
-- Dependencies: 6
-- Name: entities_tags_seq; Type: SEQUENCE; Schema: public; Owner: metaguvernare
--

CREATE SEQUENCE entities_tags_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 20;


ALTER TABLE public.entities_tags_seq OWNER TO metaguvernare;

--
-- TOC entry 1576 (class 1259 OID 49536)
-- Dependencies: 1898 6
-- Name: entities_tags; Type: TABLE; Schema: public; Owner: metaguvernare; Tablespace: 
--

CREATE TABLE entities_tags (
    entity_tag_id bigint DEFAULT nextval('entities_tags_seq'::regclass) NOT NULL,
    entity_id bigint NOT NULL,
    tag_id bigint NOT NULL
);


ALTER TABLE public.entities_tags OWNER TO metaguvernare;

--
-- TOC entry 1562 (class 1259 OID 33133)
-- Dependencies: 6
-- Name: entities_tree; Type: TABLE; Schema: public; Owner: metaguvernare; Tablespace: 
--

CREATE TABLE entities_tree (
    parent_entity_id bigint NOT NULL,
    child_entity_id bigint NOT NULL
);


ALTER TABLE public.entities_tree OWNER TO metaguvernare;

--
-- TOC entry 1569 (class 1259 OID 33213)
-- Dependencies: 1883 6
-- Name: entities_votes; Type: TABLE; Schema: public; Owner: metaguvernare; Tablespace: 
--

CREATE TABLE entities_votes (
    entity_id bigint NOT NULL,
    user_id bigint NOT NULL,
    vote character(1) NOT NULL,
    last_update timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    comment_id bigint
);


ALTER TABLE public.entities_votes OWNER TO metaguvernare;

--
-- TOC entry 1566 (class 1259 OID 33189)
-- Dependencies: 6
-- Name: entity_types_seq; Type: SEQUENCE; Schema: public; Owner: metaguvernare
--

CREATE SEQUENCE entity_types_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.entity_types_seq OWNER TO metaguvernare;

--
-- TOC entry 1560 (class 1259 OID 33093)
-- Dependencies: 1879 6
-- Name: entity_types; Type: TABLE; Schema: public; Owner: metaguvernare; Tablespace: 
--

CREATE TABLE entity_types (
    id bigint DEFAULT nextval('entity_types_seq'::regclass) NOT NULL,
    type character varying(20) NOT NULL,
    description character varying(100)
);


ALTER TABLE public.entity_types OWNER TO metaguvernare;

--
-- TOC entry 1583 (class 1259 OID 49680)
-- Dependencies: 6
-- Name: entity_types_relations; Type: TABLE; Schema: public; Owner: metaguvernare; Tablespace: 
--

CREATE TABLE entity_types_relations (
    source_entity_type_id bigint NOT NULL,
    target_entity_type_id bigint NOT NULL
);


ALTER TABLE public.entity_types_relations OWNER TO metaguvernare;

--
-- TOC entry 1588 (class 1259 OID 49787)
-- Dependencies: 6
-- Name: statuses_seq; Type: SEQUENCE; Schema: public; Owner: metaguvernare
--

CREATE SEQUENCE statuses_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.statuses_seq OWNER TO metaguvernare;

--
-- TOC entry 1587 (class 1259 OID 49782)
-- Dependencies: 1906 6
-- Name: statuses; Type: TABLE; Schema: public; Owner: metaguvernare; Tablespace: 
--

CREATE TABLE statuses (
    id bigint DEFAULT nextval('statuses_seq'::regclass) NOT NULL,
    status character varying(20) NOT NULL
);


ALTER TABLE public.statuses OWNER TO metaguvernare;

--
-- TOC entry 1575 (class 1259 OID 49533)
-- Dependencies: 6
-- Name: tags_seq; Type: SEQUENCE; Schema: public; Owner: metaguvernare
--

CREATE SEQUENCE tags_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 20;


ALTER TABLE public.tags_seq OWNER TO metaguvernare;

--
-- TOC entry 1574 (class 1259 OID 49528)
-- Dependencies: 1897 6
-- Name: tags; Type: TABLE; Schema: public; Owner: metaguvernare; Tablespace: 
--

CREATE TABLE tags (
    id bigint DEFAULT nextval('tags_seq'::regclass) NOT NULL,
    tag character varying(100) NOT NULL
);


ALTER TABLE public.tags OWNER TO metaguvernare;

--
-- TOC entry 1567 (class 1259 OID 33191)
-- Dependencies: 6
-- Name: user_types_seq; Type: SEQUENCE; Schema: public; Owner: metaguvernare
--

CREATE SEQUENCE user_types_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.user_types_seq OWNER TO metaguvernare;

--
-- TOC entry 1558 (class 1259 OID 33083)
-- Dependencies: 1877 6
-- Name: user_types; Type: TABLE; Schema: public; Owner: metaguvernare; Tablespace: 
--

CREATE TABLE user_types (
    id bigint DEFAULT nextval('user_types_seq'::regclass) NOT NULL,
    type character varying(20) NOT NULL,
    description character varying(100)
);


ALTER TABLE public.user_types OWNER TO metaguvernare;

--
-- TOC entry 1568 (class 1259 OID 33193)
-- Dependencies: 6
-- Name: users_seq; Type: SEQUENCE; Schema: public; Owner: metaguvernare
--

CREATE SEQUENCE users_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


ALTER TABLE public.users_seq OWNER TO metaguvernare;

--
-- TOC entry 1557 (class 1259 OID 33078)
-- Dependencies: 1874 1875 1876 6
-- Name: users; Type: TABLE; Schema: public; Owner: metaguvernare; Tablespace: 
--

CREATE TABLE users (
    id bigint DEFAULT nextval('users_seq'::regclass) NOT NULL,
    username character varying(50) NOT NULL,
    password character varying(50) NOT NULL,
    insert_date timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    last_login timestamp without time zone,
    default_entity_id bigint DEFAULT 1,
    email character varying(100),
    last_logout timestamp without time zone,
    user_hash character varying(32)
);


ALTER TABLE public.users OWNER TO metaguvernare;

--
-- TOC entry 1573 (class 1259 OID 49471)
-- Dependencies: 1896 6
-- Name: users_ips; Type: TABLE; Schema: public; Owner: metaguvernare; Tablespace: 
--

CREATE TABLE users_ips (
    ip character(15) NOT NULL,
    last_login timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE public.users_ips OWNER TO metaguvernare;

--
-- TOC entry 1932 (class 2606 OID 41292)
-- Dependencies: 1572 1572 1572 1572 1572
-- Name: action_strategies_pk; Type: CONSTRAINT; Schema: public; Owner: metaguvernare; Tablespace: 
--

ALTER TABLE ONLY action_strategies
    ADD CONSTRAINT action_strategies_pk PRIMARY KEY (complex_entity_type_id, user_type_id, action_id, entity_type_id);


--
-- TOC entry 1944 (class 2606 OID 49584)
-- Dependencies: 1580 1580
-- Name: action_targets_pk; Type: CONSTRAINT; Schema: public; Owner: metaguvernare; Tablespace: 
--

ALTER TABLE ONLY action_targets
    ADD CONSTRAINT action_targets_pk PRIMARY KEY (id);


--
-- TOC entry 1946 (class 2606 OID 49648)
-- Dependencies: 1580 1580
-- Name: action_targets_target_unique; Type: CONSTRAINT; Schema: public; Owner: metaguvernare; Tablespace: 
--

ALTER TABLE ONLY action_targets
    ADD CONSTRAINT action_targets_target_unique UNIQUE (target);


--
-- TOC entry 1918 (class 2606 OID 33092)
-- Dependencies: 1559 1559
-- Name: action_types_pk; Type: CONSTRAINT; Schema: public; Owner: metaguvernare; Tablespace: 
--

ALTER TABLE ONLY action_types
    ADD CONSTRAINT action_types_pk PRIMARY KEY (id);


--
-- TOC entry 1942 (class 2606 OID 49564)
-- Dependencies: 1579 1579
-- Name: actions_pk; Type: CONSTRAINT; Schema: public; Owner: metaguvernare; Tablespace: 
--

ALTER TABLE ONLY actions
    ADD CONSTRAINT actions_pk PRIMARY KEY (id);


--
-- TOC entry 1952 (class 2606 OID 49726)
-- Dependencies: 1584 1584
-- Name: activity_log_pk; Type: CONSTRAINT; Schema: public; Owner: metaguvernare; Tablespace: 
--

ALTER TABLE ONLY activity_log
    ADD CONSTRAINT activity_log_pk PRIMARY KEY (id);


--
-- TOC entry 1948 (class 2606 OID 49600)
-- Dependencies: 1582 1582
-- Name: application_config_pk; Type: CONSTRAINT; Schema: public; Owner: metaguvernare; Tablespace: 
--

ALTER TABLE ONLY application_config
    ADD CONSTRAINT application_config_pk PRIMARY KEY (param_name);


--
-- TOC entry 1930 (class 2606 OID 41287)
-- Dependencies: 1570 1570
-- Name: complex_entity_types; Type: CONSTRAINT; Schema: public; Owner: metaguvernare; Tablespace: 
--

ALTER TABLE ONLY complex_entity_type
    ADD CONSTRAINT complex_entity_types PRIMARY KEY (id);


--
-- TOC entry 1922 (class 2606 OID 33130)
-- Dependencies: 1561 1561
-- Name: entities_pk; Type: CONSTRAINT; Schema: public; Owner: metaguvernare; Tablespace: 
--

ALTER TABLE ONLY entities
    ADD CONSTRAINT entities_pk PRIMARY KEY (id);


--
-- TOC entry 1926 (class 2606 OID 33172)
-- Dependencies: 1563 1563 1563
-- Name: entities_subscribers_pk; Type: CONSTRAINT; Schema: public; Owner: metaguvernare; Tablespace: 
--

ALTER TABLE ONLY entities_subscribers
    ADD CONSTRAINT entities_subscribers_pk PRIMARY KEY (entity_id, user_id);


--
-- TOC entry 1940 (class 2606 OID 49540)
-- Dependencies: 1576 1576
-- Name: entities_tags_pk; Type: CONSTRAINT; Schema: public; Owner: metaguvernare; Tablespace: 
--

ALTER TABLE ONLY entities_tags
    ADD CONSTRAINT entities_tags_pk PRIMARY KEY (entity_tag_id);


--
-- TOC entry 1924 (class 2606 OID 33137)
-- Dependencies: 1562 1562 1562
-- Name: entities_tree_pk; Type: CONSTRAINT; Schema: public; Owner: metaguvernare; Tablespace: 
--

ALTER TABLE ONLY entities_tree
    ADD CONSTRAINT entities_tree_pk PRIMARY KEY (parent_entity_id, child_entity_id);


--
-- TOC entry 1954 (class 2606 OID 49771)
-- Dependencies: 1586 1586 1586
-- Name: entities_users_pk; Type: CONSTRAINT; Schema: public; Owner: metaguvernare; Tablespace: 
--

ALTER TABLE ONLY entities_users
    ADD CONSTRAINT entities_users_pk PRIMARY KEY (entity_id, user_id);


--
-- TOC entry 1928 (class 2606 OID 33218)
-- Dependencies: 1569 1569 1569
-- Name: entities_votes_pk; Type: CONSTRAINT; Schema: public; Owner: metaguvernare; Tablespace: 
--

ALTER TABLE ONLY entities_votes
    ADD CONSTRAINT entities_votes_pk PRIMARY KEY (entity_id, user_id);


--
-- TOC entry 1920 (class 2606 OID 33097)
-- Dependencies: 1560 1560
-- Name: entity_types_pk; Type: CONSTRAINT; Schema: public; Owner: metaguvernare; Tablespace: 
--

ALTER TABLE ONLY entity_types
    ADD CONSTRAINT entity_types_pk PRIMARY KEY (id);


--
-- TOC entry 1950 (class 2606 OID 49684)
-- Dependencies: 1583 1583 1583
-- Name: entity_types_relations_pk; Type: CONSTRAINT; Schema: public; Owner: metaguvernare; Tablespace: 
--

ALTER TABLE ONLY entity_types_relations
    ADD CONSTRAINT entity_types_relations_pk PRIMARY KEY (source_entity_type_id, target_entity_type_id);


--
-- TOC entry 1956 (class 2606 OID 49786)
-- Dependencies: 1587 1587
-- Name: statuses_pk; Type: CONSTRAINT; Schema: public; Owner: metaguvernare; Tablespace: 
--

ALTER TABLE ONLY statuses
    ADD CONSTRAINT statuses_pk PRIMARY KEY (id);


--
-- TOC entry 1936 (class 2606 OID 49532)
-- Dependencies: 1574 1574
-- Name: tags_pk; Type: CONSTRAINT; Schema: public; Owner: metaguvernare; Tablespace: 
--

ALTER TABLE ONLY tags
    ADD CONSTRAINT tags_pk PRIMARY KEY (id);


--
-- TOC entry 1938 (class 2606 OID 49674)
-- Dependencies: 1574 1574
-- Name: tags_tag_unique; Type: CONSTRAINT; Schema: public; Owner: metaguvernare; Tablespace: 
--

ALTER TABLE ONLY tags
    ADD CONSTRAINT tags_tag_unique UNIQUE (tag);


--
-- TOC entry 1916 (class 2606 OID 33087)
-- Dependencies: 1558 1558
-- Name: user_types_pk; Type: CONSTRAINT; Schema: public; Owner: metaguvernare; Tablespace: 
--

ALTER TABLE ONLY user_types
    ADD CONSTRAINT user_types_pk PRIMARY KEY (id);


--
-- TOC entry 1908 (class 2606 OID 49874)
-- Dependencies: 1557 1557
-- Name: users_email_unique; Type: CONSTRAINT; Schema: public; Owner: metaguvernare; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_email_unique UNIQUE (email);


--
-- TOC entry 1910 (class 2606 OID 49877)
-- Dependencies: 1557 1557
-- Name: users_hash_unique; Type: CONSTRAINT; Schema: public; Owner: metaguvernare; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_hash_unique UNIQUE (user_hash);


--
-- TOC entry 1934 (class 2606 OID 49868)
-- Dependencies: 1573 1573 1573 1573
-- Name: users_ips_pk; Type: CONSTRAINT; Schema: public; Owner: metaguvernare; Tablespace: 
--

ALTER TABLE ONLY users_ips
    ADD CONSTRAINT users_ips_pk PRIMARY KEY (ip, user_id, last_login);


--
-- TOC entry 1912 (class 2606 OID 33082)
-- Dependencies: 1557 1557
-- Name: users_pk; Type: CONSTRAINT; Schema: public; Owner: metaguvernare; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pk PRIMARY KEY (id);


--
-- TOC entry 1914 (class 2606 OID 33246)
-- Dependencies: 1557 1557
-- Name: users_username_unique; Type: CONSTRAINT; Schema: public; Owner: metaguvernare; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_username_unique UNIQUE (username);


--
-- TOC entry 1984 (class 2620 OID 49522)
-- Dependencies: 1561 19
-- Name: entities_last_update_trigger; Type: TRIGGER; Schema: public; Owner: metaguvernare
--

CREATE TRIGGER entities_last_update_trigger
    BEFORE INSERT OR UPDATE ON entities
    FOR EACH ROW
    EXECUTE PROCEDURE entities_last_update();


--
-- TOC entry 1985 (class 2620 OID 49741)
-- Dependencies: 1561 20
-- Name: log_activity_trigger; Type: TRIGGER; Schema: public; Owner: metaguvernare
--

CREATE TRIGGER log_activity_trigger
    AFTER INSERT OR UPDATE ON entities
    FOR EACH ROW
    EXECUTE PROCEDURE log_activity();


--
-- TOC entry 1973 (class 2606 OID 49642)
-- Dependencies: 1572 1580 1943
-- Name: action_strategies_action_targets_fk; Type: FK CONSTRAINT; Schema: public; Owner: metaguvernare
--

ALTER TABLE ONLY action_strategies
    ADD CONSTRAINT action_strategies_action_targets_fk FOREIGN KEY (action_target_id) REFERENCES action_targets(id);


--
-- TOC entry 1972 (class 2606 OID 49575)
-- Dependencies: 1579 1572 1941
-- Name: action_strategies_actions_fk; Type: FK CONSTRAINT; Schema: public; Owner: metaguvernare
--

ALTER TABLE ONLY action_strategies
    ADD CONSTRAINT action_strategies_actions_fk FOREIGN KEY (action_id) REFERENCES actions(id);


--
-- TOC entry 1971 (class 2606 OID 41313)
-- Dependencies: 1572 1570 1929
-- Name: action_strategies_complex_entity_types_fk; Type: FK CONSTRAINT; Schema: public; Owner: metaguvernare
--

ALTER TABLE ONLY action_strategies
    ADD CONSTRAINT action_strategies_complex_entity_types_fk FOREIGN KEY (complex_entity_type_id) REFERENCES complex_entity_type(id);


--
-- TOC entry 1970 (class 2606 OID 41308)
-- Dependencies: 1919 1572 1560
-- Name: action_strategies_entity_types_fk; Type: FK CONSTRAINT; Schema: public; Owner: metaguvernare
--

ALTER TABLE ONLY action_strategies
    ADD CONSTRAINT action_strategies_entity_types_fk FOREIGN KEY (entity_type_id) REFERENCES entity_types(id);


--
-- TOC entry 1969 (class 2606 OID 41298)
-- Dependencies: 1558 1915 1572
-- Name: action_strategies_user_types_fk; Type: FK CONSTRAINT; Schema: public; Owner: metaguvernare
--

ALTER TABLE ONLY action_strategies
    ADD CONSTRAINT action_strategies_user_types_fk FOREIGN KEY (user_type_id) REFERENCES user_types(id);


--
-- TOC entry 1977 (class 2606 OID 49565)
-- Dependencies: 1559 1917 1579
-- Name: actions_action_types_fk; Type: FK CONSTRAINT; Schema: public; Owner: metaguvernare
--

ALTER TABLE ONLY actions
    ADD CONSTRAINT actions_action_types_fk FOREIGN KEY (action_type_id) REFERENCES action_types(id);


--
-- TOC entry 1980 (class 2606 OID 49727)
-- Dependencies: 1917 1584 1559
-- Name: activiti_log_action_types_fk; Type: FK CONSTRAINT; Schema: public; Owner: metaguvernare
--

ALTER TABLE ONLY activity_log
    ADD CONSTRAINT activiti_log_action_types_fk FOREIGN KEY (action_type_id) REFERENCES action_types(id);


--
-- TOC entry 1960 (class 2606 OID 49483)
-- Dependencies: 1561 1929 1570
-- Name: entities_complex_entity_type_fk; Type: FK CONSTRAINT; Schema: public; Owner: metaguvernare
--

ALTER TABLE ONLY entities
    ADD CONSTRAINT entities_complex_entity_type_fk FOREIGN KEY (complex_entity_type_id) REFERENCES complex_entity_type(id);


--
-- TOC entry 1959 (class 2606 OID 33153)
-- Dependencies: 1560 1919 1561
-- Name: entities_entities_types_fk; Type: FK CONSTRAINT; Schema: public; Owner: metaguvernare
--

ALTER TABLE ONLY entities
    ADD CONSTRAINT entities_entities_types_fk FOREIGN KEY (entity_type_id) REFERENCES entity_types(id);


--
-- TOC entry 1961 (class 2606 OID 49523)
-- Dependencies: 1561 1561 1921
-- Name: entities_self_ref_fk; Type: FK CONSTRAINT; Schema: public; Owner: metaguvernare
--

ALTER TABLE ONLY entities
    ADD CONSTRAINT entities_self_ref_fk FOREIGN KEY (parent_entity_id) REFERENCES entities(id);


--
-- TOC entry 1964 (class 2606 OID 33173)
-- Dependencies: 1561 1563 1921
-- Name: entities_subscribers_entities_fk; Type: FK CONSTRAINT; Schema: public; Owner: metaguvernare
--

ALTER TABLE ONLY entities_subscribers
    ADD CONSTRAINT entities_subscribers_entities_fk FOREIGN KEY (entity_id) REFERENCES entities(id);


--
-- TOC entry 1965 (class 2606 OID 33178)
-- Dependencies: 1563 1557 1911
-- Name: entities_subscribers_users; Type: FK CONSTRAINT; Schema: public; Owner: metaguvernare
--

ALTER TABLE ONLY entities_subscribers
    ADD CONSTRAINT entities_subscribers_users FOREIGN KEY (user_id) REFERENCES users(id);


--
-- TOC entry 1975 (class 2606 OID 49541)
-- Dependencies: 1921 1576 1561
-- Name: entities_tags_entities_fk; Type: FK CONSTRAINT; Schema: public; Owner: metaguvernare
--

ALTER TABLE ONLY entities_tags
    ADD CONSTRAINT entities_tags_entities_fk FOREIGN KEY (entity_id) REFERENCES entities(id);


--
-- TOC entry 1976 (class 2606 OID 49546)
-- Dependencies: 1576 1574 1935
-- Name: entities_tags_tags_fk; Type: FK CONSTRAINT; Schema: public; Owner: metaguvernare
--

ALTER TABLE ONLY entities_tags
    ADD CONSTRAINT entities_tags_tags_fk FOREIGN KEY (tag_id) REFERENCES tags(id);


--
-- TOC entry 1962 (class 2606 OID 33138)
-- Dependencies: 1562 1921 1561
-- Name: entities_tree_entities_fk1; Type: FK CONSTRAINT; Schema: public; Owner: metaguvernare
--

ALTER TABLE ONLY entities_tree
    ADD CONSTRAINT entities_tree_entities_fk1 FOREIGN KEY (parent_entity_id) REFERENCES entities(id);


--
-- TOC entry 1963 (class 2606 OID 33143)
-- Dependencies: 1562 1561 1921
-- Name: entities_tree_entities_fk2; Type: FK CONSTRAINT; Schema: public; Owner: metaguvernare
--

ALTER TABLE ONLY entities_tree
    ADD CONSTRAINT entities_tree_entities_fk2 FOREIGN KEY (child_entity_id) REFERENCES entities(id);


--
-- TOC entry 1981 (class 2606 OID 49772)
-- Dependencies: 1586 1921 1561
-- Name: entities_users_entities_fk; Type: FK CONSTRAINT; Schema: public; Owner: metaguvernare
--

ALTER TABLE ONLY entities_users
    ADD CONSTRAINT entities_users_entities_fk FOREIGN KEY (entity_id) REFERENCES entities(id);


--
-- TOC entry 1958 (class 2606 OID 33148)
-- Dependencies: 1561 1557 1911
-- Name: entities_users_fk; Type: FK CONSTRAINT; Schema: public; Owner: metaguvernare
--

ALTER TABLE ONLY entities
    ADD CONSTRAINT entities_users_fk FOREIGN KEY (creator_id) REFERENCES users(id);


--
-- TOC entry 1983 (class 2606 OID 49790)
-- Dependencies: 1587 1586 1955
-- Name: entities_users_statuses_fk; Type: FK CONSTRAINT; Schema: public; Owner: metaguvernare
--

ALTER TABLE ONLY entities_users
    ADD CONSTRAINT entities_users_statuses_fk FOREIGN KEY (status_id) REFERENCES statuses(id);


--
-- TOC entry 1982 (class 2606 OID 49777)
-- Dependencies: 1911 1557 1586
-- Name: entities_users_users_fk; Type: FK CONSTRAINT; Schema: public; Owner: metaguvernare
--

ALTER TABLE ONLY entities_users
    ADD CONSTRAINT entities_users_users_fk FOREIGN KEY (user_id) REFERENCES users(id);


--
-- TOC entry 1966 (class 2606 OID 33219)
-- Dependencies: 1921 1569 1561
-- Name: entities_votes_entities_fk; Type: FK CONSTRAINT; Schema: public; Owner: metaguvernare
--

ALTER TABLE ONLY entities_votes
    ADD CONSTRAINT entities_votes_entities_fk FOREIGN KEY (entity_id) REFERENCES entities(id);


--
-- TOC entry 1967 (class 2606 OID 33224)
-- Dependencies: 1569 1561 1921
-- Name: entities_votes_entities_fk2; Type: FK CONSTRAINT; Schema: public; Owner: metaguvernare
--

ALTER TABLE ONLY entities_votes
    ADD CONSTRAINT entities_votes_entities_fk2 FOREIGN KEY (comment_id) REFERENCES entities(id);


--
-- TOC entry 1968 (class 2606 OID 33229)
-- Dependencies: 1569 1911 1557
-- Name: entities_votes_users_fk; Type: FK CONSTRAINT; Schema: public; Owner: metaguvernare
--

ALTER TABLE ONLY entities_votes
    ADD CONSTRAINT entities_votes_users_fk FOREIGN KEY (user_id) REFERENCES users(id);


--
-- TOC entry 1978 (class 2606 OID 49685)
-- Dependencies: 1570 1583 1929
-- Name: etr_cet_fk1; Type: FK CONSTRAINT; Schema: public; Owner: metaguvernare
--

ALTER TABLE ONLY entity_types_relations
    ADD CONSTRAINT etr_cet_fk1 FOREIGN KEY (source_entity_type_id) REFERENCES complex_entity_type(id);


--
-- TOC entry 1979 (class 2606 OID 49690)
-- Dependencies: 1570 1929 1583
-- Name: etr_cet_fk2; Type: FK CONSTRAINT; Schema: public; Owner: metaguvernare
--

ALTER TABLE ONLY entity_types_relations
    ADD CONSTRAINT etr_cet_fk2 FOREIGN KEY (target_entity_type_id) REFERENCES complex_entity_type(id);


--
-- TOC entry 1957 (class 2606 OID 33239)
-- Dependencies: 1561 1557 1921
-- Name: users_default_entity_fk; Type: FK CONSTRAINT; Schema: public; Owner: metaguvernare
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_default_entity_fk FOREIGN KEY (default_entity_id) REFERENCES entities(id);


--
-- TOC entry 1974 (class 2606 OID 49478)
-- Dependencies: 1557 1573 1911
-- Name: users_ips_users_fk; Type: FK CONSTRAINT; Schema: public; Owner: metaguvernare
--

ALTER TABLE ONLY users_ips
    ADD CONSTRAINT users_ips_users_fk FOREIGN KEY (user_id) REFERENCES users(id);


--
-- TOC entry 1990 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2010-08-03 10:55:27 EEST

--
-- PostgreSQL database dump complete
--

