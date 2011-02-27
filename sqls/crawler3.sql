select * from pending_urls
where status='W'
order by last_update desc
limit 100


update pending_urls 
set status='N'
where status='E'

select * from symbols order by id desc limit 100

select s.id,s.symbol,count(*) from symbols s, urls_symbols us
where
s.id=us.symbol_id
--and s.symbol='romania'
group by s.id,s.symbol
order by count(*) desc


select count(*) from urls_symbols
select count(*) from symbols

select pu.url from urls_symbols us, symbols s, pending_urls pu
where s.symbol='undeva'
and us.symbol_id=s.id
and pu.id=us.url_id



--delete from urls_symbols
--delete from symbols