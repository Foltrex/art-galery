alter table file_info add column directory varchar(100);
/*cache_control - 0 or null - no cache, -1 - infinite cache, any positive values is cache in minutes*/
alter table file_info add column cache_control integer;
