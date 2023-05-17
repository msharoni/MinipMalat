SELECT id,firstname, lastname, cost, (cost/(select avg(cost)
                                            from bed B
                                            where B.zip = zip))*100 as percent
FROM ((Human NATURAL JOIN Student) INNER JOIN  Rent ON Id = Studentid) NATURAL JOIN Bed
WHERE cost > (select avg(cost)
      from bed B
      where B.zip = zip)
      
