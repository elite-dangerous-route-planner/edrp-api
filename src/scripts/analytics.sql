
-- naive target list
select system_name,
  coords,
  body.name,
  total_value,
  value,
  distance_to_arrival,
  type_name,
  terraforming_state_name,
  updated_at
from body join
  (select sum(value) total_value,
     system.id,
          system.name system_name,
     system.coords
   from system join body on id = system_id
   where body.distance_to_arrival < 20000
   group by id, system.name
   having sum(value) > 3000000
   order by total_value desc
  ) summary
    on summary.id = body.system_id
order by total_value desc, system_name, distance_to_arrival;

-- point cloud for cluster analysis
select
  system.name system_name,
  system.coords,
  system.coords <-> cube(array[0.0,0.0,0.0]) distance_from_sol
from system join body on id = system_id
where distance_from_sol < 1000
and body.distance_to_arrival < 20000
group by id, system.name
having sum(value) > 2000000
order by distance_from_sol;

-- nearest neighbor
SELECT c FROM test ORDER BY c <-> cube(array[0.5,0.5,0.5]) LIMIT 1;