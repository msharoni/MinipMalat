SELECT ZIP, RoomID
FROM Elevator NATURAL JOIN Building NATURAL JOIN Room NATURAL JOIN Bed
WHERE Isoperational ='true' AND isnagish = 'true' AND Istopbunk = 'false' AND (Zip, Roomid, Bednum) NOT IN (SELECT ZIP, Roomid, Bednum
                                                                                                            FROM Rent
                                                                                                            WHERE Rent.Endofrentdate>TO_DATE('17-05-2023','DD-MM-YYYY'))
