SELECT FIRSTNAME,LASTNAME,EMAIL
FROM HUMAN INNER JOIN(SELECT DISTINCT STUDENTID 
                      FROM RENT NATURAL JOIN ROOM
                      WHERE LENGTH(PASSWORD) < 8)
           ON HUMAN.ID = STUDENTID;
