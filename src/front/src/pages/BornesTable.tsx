import { Box, Grid, ListItemButton, Typography } from "@mui/material";
import React, { useEffect } from "react";
import { InfinityScrollItemsTable, InfinityScrollItemsTableProps, PageRequest, TableColumnDefinition } from "./DisplayTable";
import { ChargePoint, searchChargePoint } from "../conf/chargePointController";
import { Link } from "react-router-dom";

const PAGE_SIZE = 10; // Max items displayed in the chargepoint table

const chargePointTableColumns: TableColumnDefinition[] = [
  {
    title: "Identifiant client",
    /*
    filter: {
      apiField: "containsTitle",
      onChange: value => console.log("Filtering on : " + value)
    }
    */
  },
  {
    title: "Étape", 
  },
  {
    title: "Status",
  },
  {
    title: "Dernière activitée",
  }
]

export function ChargePointTable() {
    const [tableData, setTableData] = React.useState<ChargePoint[]>([]);
    const [currentPage, setCurrentPage] = React.useState(0);
    const [hasMore, setHasMore] = React.useState(true);
    const [error, setError] = React.useState<string | undefined>(undefined);

    useEffect(() => {
      searchChargePoint(PAGE_SIZE).then((result: PageRequest<ChargePoint> | undefined) => {
        if(!result){
          setError("Erreur lors de la récupération des bornes.")
          return
        }
        setTableData(result.data)
        setHasMore(result.total > PAGE_SIZE)
      });
    }, [])
    

    let props: InfinityScrollItemsTableProps<ChargePoint> = {
      columns: chargePointTableColumns,
      key: "charge-point-table",
      data: tableData,
      hasMore: hasMore,
      error: error,
      onSelection: chargePoint => { console.log("Selected item : " + chargePoint.id) },
      formatter: (chargePoint) => {
        return (
          <Box key={"box-configuration-edit-path-" + chargePoint.id}  paddingTop={1} maxWidth={"true"}>
              <Link key={"chargepoint-edit-path-" + chargePoint.id}  to={{ pathname: 'display/' + chargePoint.id}} style={{ textDecoration: 'none', paddingTop: 10 }}>
                  <ListItemButton style={{maxWidth: "true", height:"5vh", padding: 0, paddingTop: 3, borderRadius: 50, color: 'black', backgroundColor: '#E1E1E1'}}>
                      <Grid container maxWidth={"true"} flexDirection={"row"} alignItems={"center"}>
                          <Grid item xs={12/chargePointTableColumns.length} maxWidth={"true"} justifyContent={"center"}>
                              <Typography variant="body1" align="center">{chargePoint.clientId}</Typography>
                          </Grid>
                          <Grid item xs={12/chargePointTableColumns.length} maxWidth={"true"} justifyContent={"center"}>
                              <Typography variant="body1" align="center">{chargePoint.status.step}</Typography>
                          </Grid>
                          <Grid item xs={12/chargePointTableColumns.length} maxWidth={"true"} justifyContent={"center"}>
                              <Typography variant="body1" align="center">{chargePoint.status.status}</Typography>
                          </Grid>
                          <Grid item xs={12/chargePointTableColumns.length} maxWidth={"true"} justifyContent={"center"}>
                              <Typography variant="body1" align="center">{new Date(chargePoint.status.lastUpdate).toLocaleString()}</Typography>
                          </Grid>
                      </Grid>
                  </ListItemButton>
              </Link>
          </Box>
        )
       },
      fetchData: () => {
        const nextPage = currentPage + 1;
        searchChargePoint(PAGE_SIZE,nextPage).then((result: PageRequest<ChargePoint> | undefined) => {
          if(!result){
            setError("Erreur lors de la récupération des bornes.")
            return
          }
          setTableData([...tableData, ...result.data])
          setHasMore(result.total > PAGE_SIZE * (nextPage + 1))
        });
        setCurrentPage(nextPage)
      },
    }

    return InfinityScrollItemsTable(props)
}