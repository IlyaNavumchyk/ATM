# Data structure

<style>
    th, td {
        text-align: center;
        vertical-align: middle;
    }
</style>

<table>
    <tr>
        <th>Field name</th>
        <th>Card number</th>
        <th>Pin code</th>
        <th>Balance</th>
        <th>Is card block?</th>
        <th>Count of invalidly <br> entered pin code</th>
        <th>Last date invalidly <br> entered pin code</th>
    </tr>
    <tr>
        <td>Type</td>
        <td>final String</td>
        <td>final String</td>
        <td>int</td>
        <td>boolean</td>
        <td>int</td>
        <td>LocalDate</td>
    </tr>
    <tr>
        <td>Constraint</td>
        <td>format <br> "dddd-dddd-dddd-dddd"</td>
        <td>format <br> "dddd"</td>
        <td>> -10^10 <br> < 10^10 </td>
        <td>true or false</td>
        <td><4</td>
        <td>format <br> "yyyy-MM-dd" <br> or null</td>
    </tr>
    <tr>
        <td>Example</td>
        <td>1111-1111-1111-1111</td>
        <td>1111</td>
        <td>-5000</td>
        <td>false</td>
        <td>0</td>
        <td>null</td>
    </tr>
</table>