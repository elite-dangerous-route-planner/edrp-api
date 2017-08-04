-- the purpose of this is to enable a fast typeahead on 11 million system names.
-- 3 characters at least, lower cased and all non-alphanumeric characters removed.

create or replace function ngrams(word text)
  returns text[]
language 'plpgsql'
immutable strict
as $$
declare result text[];
  declare theword text;
begin
  theword := regexp_replace(lower(word), '[^a-z0-9]', '', 'g');
  for i in 3 .. length(theword) loop
    result := result || substring(theword from 1 for i);
  end loop;
  return result;
end;
$$;

create or replace function ngrams_vector(words text)
  returns tsvector
language 'plpgsql'
immutable strict
as $$
begin
  return array_to_string(ngrams(words), ' ')::tsvector;
end;
$$;

create or replace function ngrams_query(words text)
  returns tsquery
language 'plpgsql'
immutable strict
as $$
begin
  return array_to_string(ngrams(words), ' & ')::tsquery;
end;
$$;

CREATE INDEX system_name_idx ON system USING GIN (ngrams_vector(name));

SELECT name FROM system WHERE ngrams_vector(name) @@ 'thequery'::tsquery order by name limit 10;