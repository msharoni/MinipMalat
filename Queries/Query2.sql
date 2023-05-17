SELECT Id, FirstName, Phone
FROM (Human NATURAL JOIN Student) INNER JOIN  Rent ON Id = Studentid NATURAL JOIN Room
WHERE (ZIP,ROOMID) IN (SELECT AC.ZIP, AC.ROOMID
              FROM AC)
      AND Room.Squarefeet>175
      AND Room.Color = 'Green'
      
              

