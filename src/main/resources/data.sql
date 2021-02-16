insert into driver (identifier, objective) values ('lofaloa', 10000);

insert into location (id, name, latitude, longitude, driver_identifier) values (0, 'Domicile', 45.555, 34.333, 'lofaloa');
insert into location (id, name, latitude, longitude, driver_identifier) values (1, 'Cinéma', 45.555, 34.333, 'lofaloa');
insert into location (id, name, latitude, longitude, driver_identifier) values (2, 'Magasin', 45.555, 34.333, 'lofaloa');
insert into location (id, name, latitude, longitude, driver_identifier) values (3, 'Parc', 45.555, 34.333, 'lofaloa');

insert into stop (id, moment, odometer_value, location) values (0, {ts '2020-01-01 12:00:00.00'}, 10000, 0);
insert into stop (id, moment, odometer_value, location) values (1, {ts '2020-01-01 12:20:01.00'}, 10034, 1);
insert into ride (id, departure_id, arrival_id, comment, traffic_condition, driver_identifier) values (0, 0, 1, 'Commentaire', 1, 'lofaloa');

insert into stop (id, moment, odometer_value, location) values (2, {ts '2020-01-01 13:00:00.00'}, 10000, 2);
insert into stop (id, moment, odometer_value, location) values (3, {ts '2020-01-01 13:20:01.00'}, 10056, 3);
insert into ride (id, departure_id, arrival_id, comment, traffic_condition, driver_identifier) values (1, 2, 3, 'Commentaire', 1, 'lofaloa');

alter sequence ride_sequence restart with 2;
alter sequence location_sequence restart with 4;
alter sequence stop_sequence restart with 4;