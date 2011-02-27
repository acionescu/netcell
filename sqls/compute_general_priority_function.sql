CREATE OR REPLACE FUNCTION get_general_priority(entity_id bigint) RETURNS bigint AS $$
DECLARE 
	pr RECORD;
	output bigint :=0;
	p_sum numeric :=0;
	p_avg numeric :=0;
	users_count numeric:=0;
	total_users numeric:=0;
	prioritiezed_entities_count numeric :=0;
BEGIN

FOR pr IN SELECT * FROM entities_priorities_count epc where epc.entity_id=entity_id LOOP 
	p_sum:=(p_sum + pr.priority*pr.count);
	users_count:=(users_count+pr.count);
END LOOP;
if(users_count > 0) then
p_avg:=(p_sum/users_count);
select into total_users count(*) from users;
--get the number of entities that got a priority from at least one user
select into prioritiezed_entities_count count(distinct(epc.entity_id)) from entities_priorities_count epc;
--output:=floor(11 - (11 - p_avg)*users_count/(total_users-1));
output:=floor(prioritiezed_entities_count+1.0 - prioritiezed_entities_count*((11.0 - p_avg)/10.0*users_count/(total_users-1.0)));
end if;	
RETURN output;
END;
$$ LANGUAGE plpgsql;



--compute user hash
CREATE OR REPLACE FUNCTION compute_user_hash(user_id bigint) RETURNS text AS $$
BEGIN
return (select md5(username||password||email||localtimestamp) from users where id=user_id);
END;
$$ LANGUAGE plpgsql;


