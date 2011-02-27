select * from pending_urls order by last_update desc
limit 100

select count(*) from pending_urls

select * from pending_urls 
where status != 'N'
order by last_update desc

update pending_urls set status='N'
, last_update=now()
where url ='http://appengine-cookbook.appspot.com/feeds'


insert into pending_urls values('http://kotaku.com/5353358/the-secret-world-which-side-are-you-on/gallery/\\/\\/kotaku.com\/5351836\/the-secret-world-secrets-surface-beta-access-teased\\')

delete from pending_urls where url = '\\'

select * from 
(
select DISTINCT on (split_part(substring(url,position('//' in url)+2),'/',1)) url,id from pending_urls
where status='N'
and strpos(url,'http') = 1
) dist
order by random()
limit $urlCount
 
