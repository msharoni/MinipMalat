SELECT T.ZIP,T.RoomID, endDate
FROM (SELECT ZIP,RoomID , MAX(ENDOFRENTDATE) as endDate
      FROM Bed NATURAL JOIN Rent
      GROUP BY ZIP,RoomID) T
WHERE endDate <= ALL (SELECT MAX(ENDOFRENTDATE)
                      FROM Bed NATURAL JOIN Rent
                      GROUP BY ZIP,RoomID)
                      
