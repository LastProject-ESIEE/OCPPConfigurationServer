import { Box, Drawer, Grid, IconButton, List, ListItem, ListItemText, Typography } from "@mui/material";
import React, { useEffect, useState } from "react";
import { wsManager } from "../pages/home/Home";
import { SnackbarProvider, useSnackbar } from "notistack";
import CloseIcon from '@mui/icons-material/Close';


const LOCAL_STORAGE_NOTIFICATION_KEY = "NOTIFICATION_MESSAGES"
const LOCAL_STORAGE_MAX_NOTIFICATION_COUNT = 1000
const LOCAL_STORAGE_CLEAR_OFFSET = 300

export type NotificationType = "ERROR" | "INFO" | "SUCCESS"

export type NotificationMessage = {
    title: string,
    type: NotificationType,
    content: string,
    date?: string,
}

export default function DisplayNotification(props: { open: boolean, onClose: () => void }) {
    const [notificationList, setNotificationlist] = useState<NotificationMessage[]>([]);

    useEffect(() => {
        // load notification from cache
        let notificationsCache = localStorage.getItem(LOCAL_STORAGE_NOTIFICATION_KEY)
        if (notificationsCache) {
            let notifications = JSON.parse(notificationsCache)
            if (notifications) {
                setNotificationlist(notifications)
            }
        }
    }, [])

    // Save notification in cache
    const saveNotification = (message: NotificationMessage) => {
        message.date = new Date().toLocaleTimeString()
        let newList = [message, ...notificationList]
        if (notificationList.length > LOCAL_STORAGE_MAX_NOTIFICATION_COUNT) {
            let reducedList = newList.slice(0, LOCAL_STORAGE_MAX_NOTIFICATION_COUNT - LOCAL_STORAGE_CLEAR_OFFSET)
            localStorage.setItem(LOCAL_STORAGE_NOTIFICATION_KEY, JSON.stringify(reducedList))
            setNotificationlist(reducedList)
            return
        }
        localStorage.setItem(LOCAL_STORAGE_NOTIFICATION_KEY, JSON.stringify(newList))
        setNotificationlist(newList)
    }

    return (
        <Box>
            <Drawer
                anchor={'right'}
                open={props.open}
                PaperProps={{style: {width: "50%"}}}
                onClose={() => props.onClose()}
                style={{margin: 5, overflow: "hidden"}}
            >
                <Box padding={1}>
                    <Box height={"5vh"}>
                        <Grid container flexDirection={"row"}>
                            <Grid container item xs={10} alignItems={"center"}>
                                <Grid item overflow={"hidden"}>
                                    <Typography noWrap variant="h5">Historique d'activités :</Typography>
                                </Grid>
                            </Grid>
                            <Grid item xs={2}>
                                <Grid container justifyContent={"right"}>
                                    <Grid item>
                                        <IconButton onClick={() => {
                                            props.onClose()
                                        }}>
                                            <CloseIcon/>
                                        </IconButton>
                                    </Grid>
                                </Grid>
                            </Grid>
                        </Grid>
                    </Box>

                    <Box height={"88vh"} overflow={"auto"} marginTop={0}>
                        <List style={{overflow: "auto", paddingTop: 0}}>
                            {notificationList.map((notif, index) => {
                                let backgroundColor = "";
                                switch (notif.type) {
                                    case "ERROR":
                                        backgroundColor = "rgba(255,0,0,0.3)"
                                        break
                                    case "INFO":
                                        backgroundColor = "rgba(24,119,210,0.4)"
                                        break
                                    case "SUCCESS":
                                        backgroundColor = "rgba(0,255,0,0.3)"
                                        break
                                }
                                return (
                                    <ListItem key={"notif-list-item-" + index} style={{
                                        paddingLeft: 5,
                                        paddingTop: 0,
                                        paddingBottom: 0,
                                        paddingRight: 5
                                    }}>
                                        <ListItemText title={notif.date + " - " + notif.title + "\n" + notif.content}
                                                      style={{/*width: 400*/}}>
                                            <Box bgcolor={backgroundColor} borderRadius={2} paddingLeft={1}>
                                                <Grid container flexDirection={"column"}>
                                                    <Grid item xs={12}>
                                                        <Typography noWrap
                                                                    variant="h6">{notif.date + " - " + notif.title}</Typography>
                                                    </Grid>
                                                    <Grid item xs={12} overflow={"hidden"}>
                                                        <Typography variant="body1">{notif.content}</Typography>
                                                    </Grid>
                                                </Grid>
                                            </Box>
                                        </ListItemText>
                                    </ListItem>
                                )
                            })}
                        </List>
                    </Box>

                    <Box height={"3vh"} marginTop={1}>
                        <Typography
                            variant="body1">{"L'historique contient " + notificationList.length + " éléments."}</Typography>
                    </Box>
                </Box>
            </Drawer>
            <SnackbarProvider maxSnack={3}>
                <NotificationItems onNotificationReceived={message => {
                    saveNotification(message)
                }}/>
            </SnackbarProvider>
        </Box>
    );
}

function NotificationItems(props: { onNotificationReceived: (message: NotificationMessage) => void }) {
    const {enqueueSnackbar} = useSnackbar();

    // Add event listener on the websocket connection
    useEffect(() => {
        let callBack = (message: NotificationMessage) => {
            let backgroundColor = "";
            switch (message.type) {
                case "ERROR":
                    backgroundColor = "error"
                    break
                case "INFO":
                    backgroundColor = "info"
                    break
                case "SUCCESS":
                    backgroundColor = "success"
                    break
            }
            enqueueSnackbar(message.title + ": " + message.content, {variant: backgroundColor as any})
            props.onNotificationReceived(message)
        }
        wsManager.addListener('notify', callBack)
        return () => {
            wsManager.removeListener('notify', callBack)
        };
    }, [props, enqueueSnackbar])


    return (
        <React.Fragment></React.Fragment>
    );
}
