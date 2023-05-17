SELECT Zip, Dateofconstruction
FROM Building NATURAL JOIN Elevator
WHERE isOperational = 'true' AND Dateofconstruction = (SELECT MIN(Dateofconstruction)
                                                       FROM Building NATURAL JOIN Elevator
                                                       WHERE isOperational = 'true')
