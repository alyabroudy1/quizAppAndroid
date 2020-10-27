// JavaScript file for the web page "World Peace Issue Tracker"
// Created by Harrison Kong
// Copyright (C) Coursera 2020

// Your web app's Firebase configuration
// Your web app's Firebase configuration
 var firebaseConfig = {
   apiKey: "AIzaSyCbnKjf6UkqAIqf3EzZV6Fu1qohMCHovbs",
   authDomain: "questions-cd3e2.firebaseapp.com",
   databaseURL: "https://questions-cd3e2.firebaseio.com",
   projectId: "questions-cd3e2",
   storageBucket: "questions-cd3e2.appspot.com",
   messagingSenderId: "539941290273",
   appId: "1:539941290273:web:f3fa38fbc067584c38be60",
   measurementId: "G-PM5Y4216E3"
 };

// Initialize Firebase
 firebase.initializeApp(firebaseConfig);
 firebase.analytics();

// Paste the web app's configuration above this line
// Our code starts below

const rootRef = firebase.database().ref("questions/");

// GET A ROOT REFERENCE to issues HERE (type along)


///-------------------------------------------------


//Task 3 ------------------------------------------
 //


//  rootRef.push (ques1);
//  rootRef.push (ques2);
//  rootRef.push (ques3);
 // rootRef.push ({
 //   "title" : "how old is sam?",
 //      "answers" : {
 //        "answer1" : {
 //          "isTrue" : false,
 //          "title" : 28
 //                    },
 //        "answer2" : {
 //          "isTrue" : true,
 //          "title" : 30
 //        },
 //        "answer3" : {
 //          "isTrue" : false,
 //          "title" : 20
 //                    }
 //      }
 // });
 // rootRef.push ({
 //   "title" : "how old is Soso?",
 //      "answers" : {
 //        "answer1" : {
 //          "isTrue" : false,
 //          "title" : 6
 //                    },
 //        "answer2" : {
 //          "isTrue" : true,
 //          "title" : 55
 //        },
 //        "answer3" : {
 //          "isTrue" : false,
 //          "title" : 12
 //                    }
 //      }
 // });
 // rootRef.push ({
 //   "title" : "how old is Jamal?",
 //      "answers" : {
 //        "answer1" : {
 //          "isTrue" : false,
 //          "title" : 16
 //                    },
 //        "answer2" : {
 //          "isTrue" : true,
 //          "title" : 85
 //        },
 //        "answer3" : {
 //          "isTrue" : false,
 //          "title" : 9
 //                    }
 //      }
 // });
 //
 // rootRef.push ({
 //   description: "Screen flashes on save",
 //   resolved: "no",
 //   severity: "moderate"
 // });

// Task 6 ------------------------------------------

// var recordRef = firebase.database().ref("issues/change-me");
//
// recordRef.update ({
//    "resolved": "yes"
// });

// Task 7 ------------------------------------------

// var recordRef = firebase.database().ref("issues/delete-me");
//
// recordRef.remove()
//    .catch(function(error) {
//      alert("Delete failed: " + error.message)
//     });
