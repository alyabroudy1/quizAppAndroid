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

const rootRef = firebase.database().ref('questions/');


let selectedFile;
console.log(window.XLSX);
document.getElementById('input').addEventListener("change", (event) => {
    selectedFile = event.target.files[0];
})

let data=[{
    "name":"jayanth",
    "data":"scd",
    "abc":"sdef"
}]


document.getElementById('button').addEventListener("click", () => {
    XLSX.utils.json_to_sheet(data, 'out.xlsx');
    if(selectedFile){
        let fileReader = new FileReader();
        fileReader.readAsBinaryString(selectedFile);
        fileReader.onload = (event)=>{
         let data = event.target.result;
         let workbook = XLSX.read(data,{type:"binary"});
         console.log(workbook);
         workbook.SheetNames.forEach(sheet => {
              let rowObject = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[sheet]);
              console.log(rowObject[0]);
              rowObject.forEach(quiz => saveQuestion(quiz));






              document.getElementById("jsondata").innerHTML = JSON.stringify(rowObject,undefined,4)
         });
        }
    }
});

function saveQuestion(question) {
  console.log(question);
rootRef.push ({
  question: question.question.toString(),
  option1: question.option1.toString(),
  option2: question.option2.toString(),
  option3: question.option3.toString(),
  option4: question.option4.toString(),
  answerNr: question.answerNr,
  answerDescription: question.answerDescription.toString(),
  categoryID: question.categoryID,
  level: question.level
});
}
