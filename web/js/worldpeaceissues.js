// JavaScript file for the web page "World Peace Issue Tracker"
// Created by Harrison Kong
// Copyright (C) Coursera 2020

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

const rootRef = firebase.database().ref('questions/');

// Task 4 ------------------------------------------


rootRef.on("value",

  (snapshot) => {
    const listTableBody = document.getElementById("list-table-body");

    // clear all the table rows first
    listTableBody.textContent = "";
var counter = 1;
    snapshot.forEach((child) => {
      question = child.val();
      console.log(question);
       var row = document.createElement("tr");
       row.innerHTML =
       "<td>" +
          "<input type='button' class='btn btn-danger' value='X' onclick='deleteQuestion(\"" + child.key + "\")'/>" +
       "</td>"+
       "<td>" +
           question.level +
      "</td>"+
      "<td>" +
          question.categoryID +
     "</td>"+
       "<td>" +
           question.answerDescription +
      "</td>"+
      "<td>" +
         question.answerNr +
     "</td>"+
     "<td>" +
        question.option4 +
    "</td>"+
    "<td>" +
       question.option3 +
   "</td>"+
   "<td>" +
      question.option2 +
  "</td>"+
  "<td>" +
     question.option1 +
 "</td>"+
 "<td>" +
    question.question +
"</td>"+
"<td>" +
   counter+
"</td>";

       listTableBody.append(row);
       counter++;
    });

  },

  (error) => {
    console.log("Error: " + error.code);
  }

);

// Task 5 ------------------------------------------

function addNewQuestion() {
  const question = document.getElementById("question-textfield").value;
  const option1 = document.getElementById("option1-textfield").value;
  const option2 = document.getElementById("option2-textfield").value;
  const option3 = document.getElementById("option3-textfield").value;
  const option4 = document.getElementById("option4-textfield").value;
  const answerNr = parseInt(document.getElementById("answerNr-textfield").value);
  const answerDescription = document.getElementById("answerDescription-textfield").value;
  const categoryID = parseInt(document.getElementById("categoryID-textfield").value);
  const level = parseInt(document.getElementById("level-textfield").value);

  if (question.length == 0) {
    alert("Question cannot be blank!");
    return;
  }

  rootRef.push ({
    question: question.toString(),
    option1: option1.toString(),
    option2: option2.toString(),
    option3: option3.toString(),
    option4: option4.toString(),
    answerNr: answerNr,
    answerDescription: answerDescription.toString(),
    categoryID: categoryID,
    level: level
  });


  document.getElementById("question-textfield").value="";
  document.getElementById("option1-textfield").value="";
  document.getElementById("option2-textfield").value="";
  document.getElementById("option3-textfield").value="";
  document.getElementById("option4-textfield").value="";
  document.getElementById("answerNr-textfield").value="";
  document.getElementById("answerDescription-textfield").value="";
  document.getElementById("categoryID-textfield").value="";
  document.getElementById("level-textfield").value="";
}

// Task 6 ------------------------------------------
/*
function updateQuestion(questionKey, newResolvedValue) {
  var recordRef = firebase.database().ref("issues/" + issueKey);

  recordRef.update ({
     "resolved": newResolvedValue
  });
}
*/
// Task 7 ------------------------------------------

function deleteQuestion(questionKey) {
  if (confirm("Are you sure?")) {
    var recordRef = firebase.database().ref("questions/"+questionKey);

    recordRef.remove()
       .catch(function(error) {
         alert("Delete failed: " + error.message)
        });
  }
}

// Utility function to encode special HTML characters so that the
// web browser does not treat them as HTML tags
// but as literal characters

function encodeHtml(str) {
  str = str.replace(/&/g, '&amp;');
  str = str.replace(/</g, '&lt;');
  str = str.replace(/>/g, '&gt;');
  str = str.replace(/ /g, '&nbsp;');
  return str;
}
