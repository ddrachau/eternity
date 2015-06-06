//clear all constraints (script fails if a constraint is not present, check manually)
//DROP CONSTRAINT ON (p:Project) ASSERT p.identifier IS UNIQUE;
//DROP CONSTRAINT ON (p:Project) ASSERT p.id IS UNIQUE;
//DROP CONSTRAINT ON (u:User) ASSERT u.identifier IS UNIQUE;
//DROP CONSTRAINT ON (u:User) ASSERT u.id IS UNIQUE;
//DROP CONSTRAINT ON (b:Booking) ASSERT b.id IS UNIQUE;
//DROP CONSTRAINT ON (r:RememberMe) ASSERT r.id IS UNIQUE;
//DROP CONSTRAINT ON (s:Session) ASSERT s.id IS UNIQUE;