SELECT e.zip, e.nexttestdate , (SELECT count(*)
           FROM needtofix ntf
           WHERE ntf.zip =  e.zip) as totalEl
FROM elevator e
WHERE isoperational = 'false'
ORDER BY totalEl DESC;
