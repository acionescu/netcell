select * from pending_urls 
where status='W'
--and position('.ro/' in  url) > 0
order by last_update desc
limit 1000

select count(*) from pending_urls where status='P'

select count(*) from pending_urls 

select * from pending_urls where id=15073

select l.dest_url_id, pu.url, count(*) from pending_urls pu, links l
where l.dest_url_id=pu.id
group by l.dest_url_id, pu.url
order by count(*) desc
limit 1000

select l.source_url_id, pu.url, count(*) from links l, pending_urls pu
where l.source_url_id = pu.id
group by l.source_url_id, pu.url
order by count(*) desc
limit 1000

select * from pending_urls
order by last_update desc
limit 1000


select status, count(*) from pending_urls 
--where status in ('P','E') 
where last_update > timestamp '2010-06-04 00:00'
group by status

select url from pending_urls 
where id in ( select source_url_id from links where dest_url_id=121078 )

select * from pending_urls
where status='P'
order by last_update desc
limit 2000

select * from pending_urls 
where url like'%next10.org%'


select * from pending_urls where status='E' order by last_update desc

update pending_urls
set status='N' where status='W'
--and last_update > timestamp '2010-04-27 00:00'


select * from links

--domains

select DISTINCT(split_part(substring(url,position('//' in url)+2),'/',1)) as domain,count(*) from pending_urls
where strpos(url,'http') = 1
and status='P'
--and split_part(substring(url,position('//' in url)+2),'/',1) like '%.ro'
group by domain
order by count(*) desc
--non domains
select * from pending_urls
where strpos(url,'ftp') = 1

--domains + inner links count
select split_part(substring(url,position('//' in url)+2),'/',1) as domain, count(*) 
from pending_urls
where strpos(url,'http') = 1
group by split_part(substring(url,position('//' in url)+2),'/',1)
order by count(*) desc


select * from pending_urls 
where split_part(substring(url,position('//' in url)+2),'/',1) = 'www.hhh.com'

--most cross referenced domains
select split_part(substring(pud.url,position('//' in pud.url)+2),'/',1), count(*) from pending_urls pus, pending_urls pud, links l
where l.dest_url_id=pud.id
and l.source_url_id = pus.id
and split_part(substring(pud.url,position('//' in pud.url)+2),'/',1) != split_part(substring(pus.url,position('//' in pus.url)+2),'/',1)
group by split_part(substring(pud.url,position('//' in pud.url)+2),'/',1)
order by count(*) desc

--most popular url


select url, count(*) from pending_urls pu, links l
where pu.id = l.dest_url_id
group by pu.id,pu.url
order by count(*) desc
limit 100

--
select count(l.*) from links l
where
l.dest_url_id in
(
select id from pending_urls 
where split_part(substring(url,position('//' in url)+2),'/',1) = 'www.appleinsider.com'
)

--symbols

select s.id,s.symbol,count(*) from symbols s, urls_symbols us
where
s.id=us.symbol_id
--and s.symbol='lemn'
group by s.id,s.symbol
order by count(*) desc


select url from pending_urls pu
where 
exists ( select 1 from urls_symbols us, symbols s where s.id=us.symbol_id and pu.id=us.url_id and s.symbol='lemn')
and exists ( select 1 from urls_symbols us, symbols s where s.id=us.symbol_id and pu.id=us.url_id and s.symbol='casa')



select * from urls_symbols 

--pending urls




select DISTINCT on (main_domain) url,id from 
(
select url,id,
case array_length(url_array,1)
when 1 then url_array[1]
when 2 then url_array[array_length(url_array,1)-1] ||'.'|| url_array[array_length(url_array,1)]
when 3 then url_array[array_length(url_array,1)-1] ||'.'|| url_array[array_length(url_array,1)]
else url_array[array_length(url_array,1)-2] ||'.'|| url_array[array_length(url_array,1)-1] ||'.'|| url_array[array_length(url_array,1)]
end main_domain
from
(
select regexp_split_to_array((split_part(substring(url,position('//' in url)+2),'/',1)), E'\\.') as url_array, url, id from pending_urls
where status='N'
and strpos(url,'http') = 1
order by random()
limit 10
) ua
) mmm




select url,id,
case array_length(url_array,1)
when 1 then url_array[1]
when 2 then url_array[array_length(url_array,1)-1] ||'.'|| url_array[array_length(url_array,1)]
when 3 then url_array[array_length(url_array,1)-1] ||'.'|| url_array[array_length(url_array,1)]
else url_array[array_length(url_array,1)-2] ||'.'|| url_array[array_length(url_array,1)-1] ||'.'|| url_array[array_length(url_array,1)]

end main_domain
from
(
select regexp_split_to_array((split_part(substring(url,position('//' in url)+2),'/',1)), E'\\.') as url_array, url, id from pending_urls
where status='N'
and strpos(url,'http') = 1
) ua
limit 100


--old get pending urls
select * from 
(
select DISTINCT on (split_part(substring(url,position('//' in url)+2),'/',1)) url,id from pending_urls
where status='N'
and strpos(url,'http') = 1
) dist
order by random()
limit $urlCount
