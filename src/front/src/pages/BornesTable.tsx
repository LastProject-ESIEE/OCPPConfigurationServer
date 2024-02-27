import { Box, Grid, Typography } from "@mui/material";
import React from "react";
import { InfinityScrollItemsTable, InfinityScrollItemsTableProps } from "./DisplayTable";
import { ChargePoint, ChargePointStatus } from "../conf/chargePointController";


var testChargePoint: ChargePoint[] = [
  {id:1, description: "Un charge point.", firmware: "v1", name: "C1"},
  {id:2, description: "Un charge point.", firmware: "v1", name: "C2"},
  {id:3, description: "Un charge point.", firmware: "v1", name: "C3"},
  {id:4, description: "Un charge point.", firmware: "v1", name: "C4"},
  {id:5, description: "Un charge point.", firmware: "v1", name: "C5"},
  {id:6, description: "Un charge point.", firmware: "v1", name: "C6"},
  {id:7, description: "Un charge point.", firmware: "v1", name: "C7"},
  {id:8, description: "Un charge point.", firmware: "v1", name: "C8"},
  {id:9, description: "Un charge point.", firmware: "v1", name: "C9"},
  {id:10, description: "Un charge point.", firmware: "v1", name: "C10"},
  {id:11, description: "Un charge point.", firmware: "v1", name: "C11"},
  {id:12, description: "Un charge point.", firmware: "v1", name: "C12"},
  {id:13, description: "Un charge point.", firmware: "v1", name: "C13"},
  {id:14, description: "Un charge point.", firmware: "v1", name: "C13"},
  {id:15, description: "Un charge point.", firmware: "v1", name: "C13"},
  {id:16, description: "Un charge point.", firmware: "v1", name: "C13"},
  {id:17, description: "Un charge point.", firmware: "v1", name: "C13"},
  {id:18, description: "Un charge point.", firmware: "v1", name: "C13"},
  {id:19, description: "Un charge point.", firmware: "v1", name: "C13"},
  {id:20, description: "Un charge point.", firmware: "v1", name: "C13"},
  {id:21, description: "Un charge point.", firmware: "v1", name: "C13"},
  {id:22, description: "Un charge point.", firmware: "v1", name: "C13"},
  {id:23, description: "Un charge point.", firmware: "v1", name: "C13"},
]
  
export function ChargePointTable() {
    const [tableData, setTableData] = React.useState(testChargePoint);
    //const [tableColumns, setTableColumns] = React.useState<ColumnDefinition[]>(columns);

    let props: InfinityScrollItemsTableProps<ChargePoint> = {
      columns: [{title: "Nom", filter: {apiField: "containsTitle"}}, {title: "Etat", filter: undefined},{title: "Step", filter: undefined}, {title: "Etat", filter: undefined},{title: "Step", filter: undefined}, {title: "Etat", filter: undefined},{title: "Step", filter: undefined},{title: "State", filter: undefined},{title: "State", filter: undefined},{title: "State", filter: undefined},{title: "State", filter: undefined},{title: "State", filter: undefined},{title: "State", filter: undefined},{title: "State", filter: undefined},{title: "State", filter: undefined}],
      key: "charge-point-table",
      data: testChargePoint,
      onSelection: arg => { },
      formatter: (arg,arg2) => {
        return (
          <Box maxWidth={"true"}>
              <Grid container maxWidth={"true"}>
                <Typography>{arg.name}</Typography>
              </Grid>
          </Box>
        )
       },
      fetchData: () => {
        console.log("Fetching")
        return [...testChargePoint,...testChargePoint]
      },

    }

    return InfinityScrollItemsTable(props)
}