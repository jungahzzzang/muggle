require('dotenv').config();
const puppeteer = require('puppeteer');

async function runSeatCrawl(theaterId) {
    if (!theaterId) throw new Error("theaterId is required");

    let browser;
    const seatData = [];

    try {
        browser = await puppeteer.launch({
            headless: "new",
            executablePath: process.env.PUPPETEER_EXECUTABLE_PATH || '/usr/bin/chromium',
            args: [
                '--no-sandbox',
                '--disable-setuid-sandbox',
                '--disable-dev-shm-usage',
                '--disable-accelerated-2d-canvas',
                '--no-zygote',
                '--single-process',
                '--disable-gpu',
            ],
            ignoreHTTPSErrors: true,
        });

        const page = await browser.newPage();
        await page.setViewport({ width: 1280, height: 800 });
        await page.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/117 Safari/537.36");

        for (let floor = 1; floor <= 4; floor++) {
            const url = `${process.env.CRAWL_SEAT_URL}/${theaterId}?type=${floor}`;
            await page.goto(url, { waitUntil: "networkidle2", timeout: 0 });

            const hasSeats = await page.$(".real");
            if (!hasSeats) break;

            const crawlSeatData = await page.evaluate((floorNum) => {
                const seatList = [];
                const zones = Array.from(document.querySelectorAll("div[class^='A'], div[class^='B'], div[class^='C'], div[class^='D'], div[class^='E'], div[class^='F'], div[class^='G'], div[class^='H']"));
                zones.forEach((zoneDiv) => {
                    const zone = zoneDiv.className.match(/^[A-Z]+/)?.[0] || "Z";
                    const rowElements = zoneDiv.querySelectorAll(".row");

                    rowElements.forEach((rowEl, rowIndex) => {
                        const seatElement = rowEl.querySelectorAll(".real");

                        seatElement.forEach((seatEl) => {
                            const seatNumber = seatEl.querySelector("p")?.innerText.trim() || "";
                            const seatId = seatEl.getAttribute("pk") || "";
                            const className = seatEl.className || "";
                            const rect = seatEl.getBoundingClientRect();
                            const isWheelChair = className.toLowerCase().includes("disabled");

                            seatList.push({
                                seatId,
                                seatNumber,
                                zone,
                                floor: floorNum.toString(),
                                row: (rowIndex + 1).toString(),
                                x: Math.round(rect.left),
                                y: Math.round(rect.top),
                                className,
                                isWheelChair
                            });
                        });
                    });
                });

                return seatList;
            }, floor);

            seatData.push(...crawlSeatData);
        }

        return seatData;
    } catch (error) {
        console.error("seat-crawl 에러:", error);
        throw error;
    } finally {
        if (browser) await browser.close();
    }
}

module.exports = runSeatCrawl;