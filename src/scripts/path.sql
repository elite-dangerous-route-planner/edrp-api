
CREATE OR REPLACE FUNCTION nextSystem(
  starting_system_id bigint,
  systems bigint[])
  RETURNS TABLE (
    system_id bigint,
    system_name varchar(80),
    name varchar(80),
    distance_to_arrival int,
    type_name varchar(80),
    value int
  )
LANGUAGE 'plpgsql'
IMMUTABLE STRICT

AS $$
begin

  return query select
     destination.id system_id,
     destination.name system_name,
     body.name,
     body.distance_to_arrival distance_to_arrival,
     body.type_name type_name,
     body.value
  from
     (select b.*
      from system a join system b
      on a.id = starting_system_id
      and b.id = any(systems)
      order by a.coords <-> b.coords
      limit 1) destination
  join body
  on body.system_id = destination.id
  order by body.distance_to_arrival;
end;
$$;

select * from nextSystem(17072, '{15756,90,5161,288,231,15493,12357,169,352,243,15959,13934,113,4575,192,14762,7955,8375,27,26687,17753,32231,6883,1350650,37423,7370,10039,43702,607512,332837,449884,7948,164845,197858,29144,6787}');


