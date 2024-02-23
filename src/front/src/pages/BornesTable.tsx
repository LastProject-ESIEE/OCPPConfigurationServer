import { Box } from "@mui/material";
import React from "react";
import { useEffect } from "react";
import { ColumnDefinition, ReactTabulator, ReactTabulatorOptions } from "react-tabulator";
import 'react-tabulator/css/bootstrap/tabulator_bootstrap.min.css';
import 'react-tabulator/lib/styles.css';
import { Tabulator } from "tabulator-tables";


const columns: ColumnDefinition[] = [
    { title: "Name", field: "name", width: 150, headerFilter:"input" },
    { title: "Age", field: "age", hozAlign: "left", formatter: "progress" },
    { title: "Favourite Color", field: "col" },
    { title: "Date Of Birth", field: "dob", hozAlign: "center" },
    { title: "Rating", field: "rating", hozAlign: "center", formatter: "star" },
    { title: "Passed?", field: "passed", hozAlign: "center", formatter: "tickCross" }
  ];

var data = [
    {id:1, name:"Oli Bob", age:"12", col:"red", dob:""},
    {id:2, name:"Mary May", age:"1", col:"blue", dob:"14/05/1982"},
    {id:3, name:"Christine Lobowski", age:"42", col:"green", dob:"22/05/1982"},
    {id:4, name:"Brendon Philips", age:"125", col:"orange", dob:"01/08/1980"},
    {id:5, name:"Margret Marmajuke", age:"16", col:"yellow", dob:"31/01/1999"},
  ];


const options: ReactTabulatorOptions = {
    //ajaxURL:"/exampledata/ajaxprogressive?page=5&size=20",
    progressiveLoad: "scroll",
    paginationSize: 20,
  };
  
export function ChargePointTable() {
    const [tableData, setTableData] = React.useState(data);
    const [tableColumns, setTableColumns] = React.useState<ColumnDefinition[]>(columns);
/*
    useEffect(() => {
        // Build Tabulator
        var tabulator = new Tabulator("#example-table", {
            layout: "fitColumns",
            ajaxURL: "https://tabulator.info/exampledata/ajaxprogressive?page=5&size=20",
            progressiveLoad: "scroll",
            paginationSize: 20,
            placeholder: "No Data Set",
            columns: [
                {title:"Name", field:"name", sorter:"string", width:200},
                {title:"Progress", field:"progress", sorter:"number", formatter:"progress"},
                {title:"Gender", field:"gender", sorter:"string"},
                {title:"Rating", field:"rating", formatter:"star", hozAlign:"center", width:100},
                {title:"Favourite Color", field:"col", sorter:"string"},
                {title:"Date Of Birth", field:"dob", sorter:"date", hozAlign:"center"},
                {title:"Driver", field:"car", hozAlign:"center", formatter:"tickCross", sorter:"boolean"},
            ],
        });

        // Cleanup function to destroy the Tabulator instance when the component unmounts
        return () => {
            tabulator.destroy();
        };
    }, []); // Empty dependency array ensures that this effect runs only once after the initial render
    
*/


    return (
        <Box maxWidth={"true"} maxHeight={100}>
            <ReactTabulator
                data={tableData}
                columns={tableColumns}
                layout={"fitData"}
                options={options}
                events={{
                    dataLoaded: function (dataO:any) {
                      console.log('dataLoaded', dataO);
                      // return data; //return the response data to tabulator
                      let modResponse: any = {};
                      modResponse.data = dataO;
                      modResponse.last = 5;
                      return modResponse;
                    },
                    ajaxError: function (error: any) {
                      console.log('ajaxError', error);
                    }
                }}
            />
        </Box> 
    )
}

/*
            <ReactTabulator
                data={data}
                columns={columns}
                layout={"fitData"}
                ajaxURL="/exampledata/ajaxprogressive"
                progressiveLoad="scroll"
                paginationSize={20}
                placeholder="No Data Set"
            />
export class ChargePointTableTest extends React.Component {
    el: any = React.createRef();
  
    tabulator: Tabulator | null  = null; //variable to hold your table
    tableData = []; //data for table to display
  
    componentDidMount() {
      //instantiate Tabulator when element is mounted
      this.tabulator = new Tabulator(this.el, {
        data: this.tableData, //link data to table
        reactiveData:true, //enable data reactivity
        columns: [
            {title: "Name", field: "name", sorter: "string", width: 200},
            {title: "Progress", field: "progress", sorter: "number", formatter: "progress"},
            {title: "Gender", field: "gender", sorter: "string"},
            {title: "Rating", field: "rating", formatter: "star", hozAlign: "center", width: 100},
            {title: "Favourite Color", field: "col", sorter: "string"},
            {title: "Date Of Birth", field: "dob", sorter: "date", hozAlign: "center"},
            {title: "Driver", field: "car", hozAlign: "center", formatter: "tickCross", sorter: "boolean"},
        ],
      });
    }
  
    //add table holder element to DOM
    render(){
      return (<div ref={el => (this.el = el)} />);
    }
  }
  */