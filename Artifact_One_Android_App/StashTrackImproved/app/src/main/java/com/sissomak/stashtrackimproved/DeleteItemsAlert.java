package com.sissomak.sqldemo;

import androidx.appcompat.app.AlertDialog;

/**
 * DeleteItemsAlert Class
 * Functionality:
 *      - Enables popup alert to confirm clearing the database
 *
 * @author Aaron Sissom
 * @course CS-499 CS Capstone
 * @school Southern New Hampshire University
 */

public class DeleteItemsAlert
{
    public static AlertDialog doubleButton(final ItemList context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.delete_alert_title)
                .setIcon(R.drawable.delete_all)
                .setCancelable(false)
                .setMessage(R.string.delete_alert_message_text)
                .setPositiveButton(R.string.delete_alert_yes_button, (dialog, arg1) -> {
                    ItemList.AffirmativeDelete();
                    dialog.cancel();
                })
                .setNegativeButton(R.string.delete_alert_no_button, (dialog, arg1) -> {
                    ItemList.NegativeDelete();
                    dialog.cancel();
                });

        return builder.create();
    }
}
