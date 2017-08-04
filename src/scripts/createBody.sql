-- Table: public.body

-- DROP TABLE public.body;

CREATE TABLE public.body
(
  system_id bigint NOT NULL,
  name character varying(80) COLLATE pg_catalog."default" NOT NULL,
  updated_at timestamp without time zone,
  type_id integer,
  type_name character varying(80) COLLATE pg_catalog."default",
  distance_to_arrival integer,
  terraforming_state_id integer,
  terraforming_state_name character varying(80) COLLATE pg_catalog."default",
  value integer,
  CONSTRAINT body_pkey PRIMARY KEY (system_id, name)
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.body
  OWNER to spatial;

-- Index: body_distance_to_arrival_idx

-- DROP INDEX public.body_distance_to_arrival_idx;

CREATE INDEX body_distance_to_arrival_idx
  ON public.body USING btree
  (distance_to_arrival)
TABLESPACE pg_default;

-- Index: body_system_id_idx

-- DROP INDEX public.body_system_id_idx;

CREATE INDEX body_system_id_idx
  ON public.body USING btree
  (system_id)
TABLESPACE pg_default;

-- Index: body_terraforming_state_id_idx

-- DROP INDEX public.body_terraforming_state_id_idx;

CREATE INDEX body_terraforming_state_id_idx
  ON public.body USING btree
  (terraforming_state_id)
TABLESPACE pg_default;

-- Index: body_type_id_idx

-- DROP INDEX public.body_type_id_idx;

CREATE INDEX body_type_id_idx
  ON public.body USING btree
  (type_id)
TABLESPACE pg_default;