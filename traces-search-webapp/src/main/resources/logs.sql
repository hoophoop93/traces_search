--
-- PostgreSQL database dump
--

SET client_encoding = 'UTF8';

CREATE TABLE Settings (
    idsettings int8 NOT NULL,
    pathoffset varchar(150)NOT NULL,
    linesBefore int8 NOT NULL,
    linesAfter int8 NOT NULL
);
INSERT INTO public.settings(
	idsettings, pathoffset, linesbefore, linesafter)
	VALUES (1, 'C:\\logi\\wl\\domains\\PROD2\\servers', 100, 100);

ALTER TABLE ONLY Settings
    ADD CONSTRAINT "Settings_pkey" PRIMARY KEY (idsettings);

