//package com.tlt.georepo.manager.db
//
//import io.realm.FieldAttribute
//import io.realm.DynamicRealm
//import io.realm.RealmMigration
//
//class GeoMigration : RealmMigration {
//    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
//        var oldVersion = oldVersion
//
//        // DynamicRealm exposes an editable schema
//        val schema = realm.schema
//
//        // Migrate to version 1: Add a new class.
//        // Example:
//        // public Person extends RealmObject {
//        //     private String name;
//        //     private int age;
//        //     // getters and setters left out for brevity
//        // }
//        if (oldVersion == 0L) {
//            schema.create("Person")
//                    .addField("name", String::class.java)
//                    .addField("age", Int::class.javaPrimitiveType)
//            oldVersion++
//        }
//
//        // Migrate to version 2: Add a primary key + object references
//        // Example:
//        // public Person extends RealmObject {
//        //     private String name;
//        //     @PrimaryKey
//        //     private int age;
//        //     private Dog favoriteDog;
//        //     private RealmList<Dog> dogs;
//        //     // getters and setters left out for brevity
//        // }
//
//        if (oldVersion == 1L) {
//            schema.create("Person")
//                .addField("name", String::class.java)
//                .addField("age", String::class.java)
//                .addField("favoriteDog", String::class.java)
//                .addField("dogs", String::class.java)
//            oldVersion++
//        }
//
////        if (oldVersion == 2L) {
////            schema.get("Person")!!
////                    .addField("id", Long::class.javaPrimitiveType!!, FieldAttribute.PRIMARY_KEY)
////                    .addRealmObjectField("favoriteDog", schema.get("Dog")!!)
////                    .addRealmListField("dogs", schema.get("Dog")!!)
////            oldVersion++
////        }
//    }
//}