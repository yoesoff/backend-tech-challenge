FROM python:3.11-slim
WORKDIR /app

COPY python-libs.txt .
RUN pip install --no-cache-dir -r python-libs.txt

COPY listing_service.py .

EXPOSE 6000

CMD ["python", "listing_service.py", "--port=6000", "--debug=false"]
