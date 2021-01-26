package presenter;

import java.util.List;

/**
 * TableGenerator is a tool for displays am array of data as a formatted table.
 * */
public class TableGenerator {
    /**
     * Print the data as table.
     * @param header the table header
     * @param rows the data of the table
     * */
    public void printAsTable(List<String> header, List<List<String>> rows) {
        //int[] maxWidth = new int[rows.get(0).size()];
        int[] maxWidth = new int[header.size()];
        rows.add(0,header);
        // calculate the max width for each column
        for (List<String> row : rows) {
            for (int i = 0; i < row.size(); i++) {
                maxWidth[i] = Math.max(maxWidth[i], row.get(i).length());
            }
        }

        StringBuilder formatBuilder = new StringBuilder();
        for (int w : maxWidth) {
            formatBuilder.append("%-").append(w + 2).append("s");
        }
        String format = formatBuilder.toString();

        StringBuilder table = new StringBuilder();
        for (List<String> row : rows) {
            table.append(String.format(format, row.toArray(new String[0]))).append("\n");
        }

        //print the table
        System.out.println(table);
    }

    /**
     * Set the list of strings as fixed padding.
     * */
    public String formatTitles(List<String> titles, int padding) {
        String res = "";
        for(String t : titles) {
            res += padRight(t, padding);
        }
        return res;
    }

    private String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }
}
