import React, { useEffect } from "react";
import { Tabulator } from "tabulator-tables";

export function ChargePointTable() {
  useEffect(() => {
    // Build Tabulator
    var tabulator = new Tabulator("#example-table", {
      layout: "fitColumns",
      ajaxURL: "https://tabulator.info/exampledata/ajaxprogressive?page=5&size=20",
      progressiveLoad: "scroll",
      paginationSize: 20,
      placeholder: "No Data Set",
      columns: [
        { title: "Name", field: "name", sorter: "string", width: 200 },
        { title: "Progress", field: "progress", sorter: "number", formatter: "progress" },
        { title: "Gender", field: "gender", sorter: "string" },
        { title: "Rating", field: "rating", formatter: "star", hozAlign: "center", width: 100 },
        { title: "Favourite Color", field: "col", sorter: "string" },
        { title: "Date Of Birth", field: "dob", sorter: "date", hozAlign: "center" },
        { title: "Driver", field: "car", hozAlign: "center", formatter: "tickCross", sorter: "boolean" },
      ],
    });
    
    // Cleanup function to destroy the Tabulator instance when the component unmounts
    return () => {
      tabulator.destroy();
    };
  }, []); // Empty dependency array ensures that this effect runs only once after the initial render

  return <div id="example-table"></div>;
}