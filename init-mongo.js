db = db.getSiblingDB('ingressos_db');
db.createUser({
  user: 'ingressos_user',
  pwd: 'ingressos123',
  roles: [
    {
      role: 'readWrite',
      db: 'ingressos_db'
    }
  ]
});

// Create collections
db.createCollection('usuario');
db.createCollection('evento');
db.createCollection('ingresso');
