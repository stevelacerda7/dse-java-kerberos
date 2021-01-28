# java-dse-kerberos-example
Using java driver 4.9.0 with dse and kerberos

# Keytab example
```
~/code/testing-connections $ klist -e -kt cassandra.keytab
Keytab name: FILE:cassandra.keytab
KVNO Timestamp           Principal
---- ------------------- ------------------------------------------------------
   2 01/22/2021 14:03:38 cassandra@lacerda-kerberos (DEPRECATED:arcfour-hmac)
   2 01/22/2021 14:03:38 cassandra@lacerda-kerberos (DEPRECATED:des-hmac-sha1)
   2 01/22/2021 14:03:38 cassandra@lacerda-kerberos (DEPRECATED:des-cbc-md5)
```

# DSE roles
```
cassandra@cqlsh> list roles;

 role                             | super | login | options
----------------------------------+-------+-------+---------
                        cassandra |  True |  True |        {}
       cassandra@lacerda-kerberos |  True |  True |        {}
```

# dse.yaml
```
authentication_options:
    other_schemes:
    -   internal
    scheme_permissions: false
    default_scheme: kerberos
    enabled: true
<<<<<<< HEAD
    
=======

>>>>>>> fe3388f8428c4d0e2784ddb1038acf3e0acb2623
kerberos_options:
    keytab: /home/automaton/ctool_security/dse.keytab
    service_principal: dse_automaton/_HOST@lacerda-kerberos
    http_principal: HTTP/_HOST@lacerda-kerberos
    qop: auth
<<<<<<< HEAD
```    


=======
```
>>>>>>> fe3388f8428c4d0e2784ddb1038acf3e0acb2623
