import { Box, Button, Container, Grid } from "@mui/material";
import { useEffect, useState } from "react";
import FormInput from "../../../sharedComponents/FormInput";
import { TypeAllowed, getTypeAllowed, postCreateFirmware } from "../../../conf/FirmwareController";
import SelectItemsList, { KeyValueItem } from "../../../sharedComponents/SelectItemsList";


type CreateOrEditFirmwareProps = {
    version: string,
    url: string,
    constructeur: string,
}


export type CreateFirmwareFormData = {
    version: string,
    url: string,
    constructeur: string,
    typesAllowed: Set<TypeAllowed>
}


export default function CreateFirmware(props: {data?: CreateFirmwareFormData}) {
    const [formData, setFormData] = useState<CreateFirmwareFormData>(props.data ?? {
        version: "",
        url: "",
        constructeur: "",
        typesAllowed: new Set<TypeAllowed>()
    });
    const [typeAllowedList, setTypeAllowedList] = useState<KeyValueItem<TypeAllowed>[]>([]);
    const [selectedItems, setSelectedItems] = useState<KeyValueItem<TypeAllowed>[]>([]);

    // Fetch first charge point page on component load
    useEffect(() => {
        getTypeAllowed().then((result: TypeAllowed[] | undefined) => {
            if(!result){
                return
            }
            setTypeAllowedList(result.map(v => {
                return {
                    item: v,
                    selected: false,
                    label: v.constructor + "-" + v.type,
                    value: "",
                    id: v.id + "",
                    checker: inputValue => inputValue !== ""
                }
            }))
        });
        }, [])

    return (
        <Container maxWidth="xl" sx={{mt: 4, mb: 4}}>
            <Grid container spacing={15}>
                <Grid item xs={12} md={6}>
                    <Box>
                        <FormInput name={"Version"}
                                   onChange={val => setFormData(prevState => {
                                       return {...prevState, version: val}
                                   })}
                                   checkIsWrong={value => value === "abc"}
                                   value={formData.version}
                        />
                        <FormInput name={"URL"}
                                   onChange={val => setFormData(prevState => {
                                    return {...prevState, url: val}
                                   })}
                                   checkIsWrong={value => value === "abc"}
                                   value={formData.url}
                        />
                        <FormInput name={"Constructeur"}
                                   onChange={val => setFormData(prevState => {
                                    return {...prevState, constructeur: val}
                                   })}
                                   checkIsWrong={value => value === "abc"}
                                   value={formData.constructeur}
                        />
                    </Box>
                    <Box
                        sx={{
                            display: 'flex',
                            justifyContent: 'center',
                            boxSizing: 'border-box',
                        }}
                        pt={2}
                    >
                        <Button sx={{borderRadius: 28}} variant="contained" color="primary"
                                onClick={() => {
                                    
                                    let typesAllowed = new Set<TypeAllowed>
                                    selectedItems.forEach(item => {
                                        typesAllowed.add(item.item)
                                    })
        
                                    let firmware: CreateFirmwareFormData = {
                                        constructeur: formData.constructeur,
                                        url: formData.url,
                                        typesAllowed: typesAllowed,
                                        version: formData.version,
                                    }
                                    postCreateFirmware(firmware)
                                }
                                }>Valider</Button>
                    </Box>

                </Grid>
                <Grid item xs={12} md={6}>
                    <SelectItemsList 
                        title={"Modèles compatibles"}
                        keyTitle={"Modèles"}
                        items={typeAllowedList} 
                        selectKind="values"
                        setSelectedItems={setSelectedItems}
                        selectedItems={selectedItems}
                        validation={items => {
                            let typesAllowed = new Set<TypeAllowed>
                            items.forEach(item => {
                                typesAllowed.add(item.item)
                            })
                            let firmware: CreateFirmwareFormData = {
                                constructeur: formData.constructeur,
                                url: formData.url,
                                typesAllowed: typesAllowed,
                                version: formData.version,
                            }
                            postCreateFirmware(firmware)
                            return true;
                        }}
                    />
                </Grid>
            </Grid>
        </Container>
    );
}


