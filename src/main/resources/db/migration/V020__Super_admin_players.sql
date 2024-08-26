alter table player add column is_super_admin boolean default false;
update player set is_super_admin = true where player_id = '291857466491273218'; -- Zsinj player ID