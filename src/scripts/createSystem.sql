
CREATE EXTENSION cube;

CREATE TABLE public.system
(
  id bigint NOT NULL,
  name character varying(80) COLLATE pg_catalog."default",
  coords cube,
  CONSTRAINT system_pkey PRIMARY KEY (id)
);

CREATE INDEX system_coords_idx
  ON public.system USING gist
  (coords)
TABLESPACE pg_default;

CREATE UNIQUE INDEX system_id_idx
  ON public.system USING btree
  (id)
TABLESPACE pg_default;

CREATE INDEX system_name_idx
  ON public.system USING gin
  (to_tsvector('english'::regconfig, name::text))
TABLESPACE pg_default;