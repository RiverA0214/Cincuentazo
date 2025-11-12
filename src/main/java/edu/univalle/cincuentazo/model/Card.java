package edu.univalle.cincuentazo.model;

public enum Card {
    //Clubs
    C01("c01.png"), C02("c02.png"), C03("c03.png"), C04("c04.png"),
    C05("c05.png"), C06("c06.png"), C07("c07.png"), C08("c08.png"),
    C09("c09.png"), C10("c10.png"), C11("c11.png"), C12("c12.png"), C13("c13.png"),

    //Diamonds
    D01("d01.png"), D02("d02.png"), D03("d03.png"), D04("d04.png"),
    D05("d05.png"), D06("d06.png"), D07("d07.png"), D08("d08.png"),
    D09("d09.png"), D10("d10.png"), D11("d11.png"), D12("d12.png"), D13("d13.png"),

    //Hearts
    H01("h01.png"), H02("h02.png"), H03("h03.png"), H04("h04.png"),
    H05("h05.png"), H06("h06.png"), H07("h07.png"), H08("h08.png"),
    H09("h09.png"), H10("h10.png"), H11("h11.png"), H12("h12.png"), H13("h13.png"),

    //Spades
    S01("s01.png"), S02("s02.png"), S03("s03.png"), S04("s04.png"),
    S05("s05.png"), S06("s06.png"), S07("s07.png"), S08("s08.png"),
    S09("s09.png"), S10("s10.png"), S11("s11.png"), S12("s12.png"), S13("s13.png");

    private final String fileName;

    Card(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getResourcePath() {
        return "/edu/univalle/cincuentazo/cards/" + fileName;
    }
}
