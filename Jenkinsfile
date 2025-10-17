pipeline {
    agent any

    tools {
        jdk 'Java'        // Use the exact name from Global Tool Configuration
        gradle 'Gradle'   // Use the exact name from Global Tool Configuration
    }

    environment {
        ALLURE_RESULTS = 'build/allure-results'
        ALLURE_REPORT  = 'build/allure-report'
    }

    stages {
        stage('Checkout from GitHub') {
            steps {
                echo '🔄 Checking out source code from GitHub...'
                checkout scm
            }
        }

        stage('Build Project') {
            steps {
                echo '🏗️ Building Gradle project (skipping tests)...'
                bat './gradlew clean build -x test'
            }
        }

        stage('Run API Tests') {
            steps {
                echo '🧪 Running API test suite with TestNG...'
                bat './gradlew clean test --no-daemon'
            }
            post {
                always {
                    echo '📦 Archiving Allure test results...'
                    archiveArtifacts artifacts: "${ALLURE_RESULTS}/**", allowEmptyArchive: true
                }
            }
        }

        stage('Generate Allure Report') {
            steps {
                echo '📊 Generating Allure HTML report...'
                bat './gradlew allureReport'
            }
        }

        stage('Publish Allure Report in Jenkins') {
            steps {
                echo '🚀 Publishing Allure Report to Jenkins dashboard...'
                allure([
                    includeProperties: false,
                    jdk: '',
                    results: [[path: "${ALLURE_RESULTS}"]]
                ])
            }
        }
    }

    post {
        success {
            echo '✅ Build & Test Successful!'
        }
        failure {
            echo '❌ Build Failed — Check logs and Allure Report for details.'
        }
        always {
            echo '🧹 Cleaning workspace after build...'
            cleanWs()
        }
    }
}
