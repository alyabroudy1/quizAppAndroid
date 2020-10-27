row.innerHTML = "<td>" + issue.severity + "</td><td>" + issue.description + "</td><td>" +

  "<select onchange='updateIssue(\"" + child.key + "\", this.value)'>" +
  "<option value='no'" + (issue.resolved=="no" ? " selected" : "") + ">no</option>" +
  "<option value='yes'" + (issue.resolved=="yes" ? " selected" : "") + ">yes</option>" +
"</select>"

+ "</td>";
