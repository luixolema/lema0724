-- this is just as an example of date so the db is not empty in order to test manually

INSERT INTO tool_type (id, name, daily_charge, weekday_charge, weekend_charge, holiday_charge)
VALUES (1, 'Ladder', 1.99, TRUE, TRUE, FALSE),
       (2, 'Chainsaw', 1.49, TRUE, FALSE, TRUE),
       (3, 'Jackhammer', 2.99, TRUE, FALSE, FALSE);


INSERT INTO tool (id, tool_code, brand, tool_type_id)
VALUES (5, 'CHNS', 'Stihl', 2),  -- Chainsaw (tool_type_id = 2)
       (6, 'LADW', 'Werner', 1), -- Ladder (tool_type_id = 1)
       (7, 'JAKD', 'DeWalt', 3), -- Jackhammer (tool_type_id = 3)
       (8, 'JAKR', 'Ridgid', 3); -- Jackhammer (tool_typ_id = 3)
