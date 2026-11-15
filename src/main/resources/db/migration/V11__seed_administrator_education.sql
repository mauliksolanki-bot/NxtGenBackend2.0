-- Seeds sample education history for the demo administrator account so the
-- Education grid on the profile page has data to render out of the box.
UPDATE NXTGEN_APP_USER_MASTER
SET EDUCATION = '[
  {"degree":"Bachelor of Technology","institution":"National Institute of Technology","university":"NIT","yearOfPassing":"2014","percentage":"82%"},
  {"degree":"Higher Secondary","institution":"St. Xavier''s School","university":"CBSE","yearOfPassing":"2010","percentage":"91%"}
]'
WHERE EMP_EMAIL = 'administrator@nxtgen.com';
