select DISTINCT on (split_part(substring(url,position('//' in url)+2),'/',1)) url , id from pending_urls
where status='N'
and strpos(url,'http') = 1
limit $urlCount

select url,id from pending_urls
where status='N'
and strpos(url,'http') = 1
order by random()
limit $urlCount

