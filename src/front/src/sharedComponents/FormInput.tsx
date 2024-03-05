import { Grid, Input, Paper } from "@mui/material";
import React, { useEffect, useState } from "react";
import Typography from "@mui/material/Typography";

export type FormInputProps = {
    name: string,
    backgroundColor?: string,
    required?: boolean,
    onChange: (newValue: string) => void,
    checkIsWrong?: (value: string) => boolean,
    placeholder?: string,
    value?: string,
    isPassword?: boolean,
    onError?: () => void
}

function FormInput({
                       name,
                       backgroundColor = 'rgb(249, 246, 251)',
                       required = false,
                       onChange,
                       checkIsWrong = val => false,
                       placeholder = name,
                       value = "",
                       isPassword = false,
                       onError = () => {}
                   }: FormInputProps) {
    const [actualBackground, setActualBackground] = useState(backgroundColor);

    useEffect(() => {
        if (checkIsWrong(value)) {
            setActualBackground('rgba(255, 0, 0, 0.2)')
            onError()
        } else {
            setActualBackground(backgroundColor)
        }
    }, [value, backgroundColor, checkIsWrong, onError]);


    return (
        <Paper elevation={2} sx={{p: 2, pt: 0, mt: 3, backgroundColor: actualBackground}}>
            <Grid direction={"column"} container justifyContent="space-between">
                <Grid item>
                    <Typography sx={{mt: 1}} variant={"h6"}>{name} : </Typography>
                </Grid>
                <Grid item>
                    <Input
                        value={value}
                        sx={{mt: 1}}
                        onChange={event => {
                            onChange(event.target.value)
                            setCurrentValue(event.target.value)
                        }}
                        type={isPassword ? "password" : "text"} 
                        onChange={event => onChange(event.target.value)}
                        fullWidth={true}
                        placeholder={placeholder}
                        required={required}
                        error={checkIsWrong(value)}
                    />
                </Grid>
            </Grid>
        </Paper>
    )
}

export default FormInput;